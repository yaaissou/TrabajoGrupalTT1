package com.trabajo.servidor.service;

import com.trabajo.servidor.model.Barco;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HundirFlotaServiceImpl implements HundirFlotaService {

    private static final int BOARD_SIZE = 10;
    private static final int DISPAROS_MAX = 50;

    private static final String COLOR_AGUA    = "#4488cc";
    private static final String COLOR_ALFA    = "#22c55e";
    private static final String COLOR_BETA    = "#20d06d";
    private static final String COLOR_GAMMA   = "#14b8a6";
    private static final String COLOR_FALLO   = "#99ccff";
    private static final String COLOR_TOCADO  = "orange";
    private static final String COLOR_HUNDIDO = "#cc0000";

    private final ConcurrentHashMap<Integer, String> partidas = new ConcurrentHashMap<>();
    private final AtomicInteger tokenCounter = new AtomicInteger(1);

    @Override
    public int generarToken() {
        return tokenCounter.getAndIncrement();
    }

    @Override
    public int simularPartida(Map<Integer, Integer> nums) {
        int token = generarToken();
        partidas.put(token, ejecutarSimulacion(nums));
        return token;
    }

    @Override
    public void procesarSolicitud(int token, Map<Integer, Integer> nums) {
        partidas.put(token, ejecutarSimulacion(nums));
    }

    @Override
    public String obtenerRawData(int token) {
        return partidas.getOrDefault(token, null);
    }

    private String ejecutarSimulacion(Map<Integer, Integer> nums) {
        int nAlfa  = nums.getOrDefault(1, 0);
        int nBeta  = nums.getOrDefault(2, 0);
        int nGamma = nums.getOrDefault(3, 0);

        int maxCeldas = (int)(BOARD_SIZE * BOARD_SIZE * 0.5);
        List<Integer> tamanos = new ArrayList<>();
        int totalCeldas = 0;
        for (int i = 0; i < nAlfa  && totalCeldas + 1 <= maxCeldas; i++) { tamanos.add(1); totalCeldas += 1; }
        for (int i = 0; i < nBeta  && totalCeldas + 2 <= maxCeldas; i++) { tamanos.add(2); totalCeldas += 2; }
        for (int i = 0; i < nGamma && totalCeldas + 3 <= maxCeldas; i++) { tamanos.add(3); totalCeldas += 3; }

        if (tamanos.isEmpty()) {
            tamanos.add(2);
        }

        Random rand = new Random();
        boolean[][] ocupado = new boolean[BOARD_SIZE][BOARD_SIZE];
        List<Barco> barcos = colocarBarcos(tamanos, ocupado, rand);

        int[][] estado = new int[BOARD_SIZE][BOARD_SIZE];
        for (Barco b : barcos) {
            for (int[] pos : b.getPositions()) {
                estado[pos[0]][pos[1]] = 1;
            }
        }

        List<int[]> disparos = generarDisparos(rand);

        Set<String> barcosCeldas = new HashSet<>();
        for (Barco b : barcos) {
            for (int[] p : b.getPositions()) barcosCeldas.add(p[0] + "," + p[1]);
        }

        int turnoFinal = 0;
        for (int t = 0; t < disparos.size(); t++) {
            int[] disparo = disparos.get(t);
            int r = disparo[0], c = disparo[1];

            if (estado[r][c] == 1) {
                estado[r][c] = -2;
                Barco barco = buscarBarco(barcos, r, c);
                if (barco != null && todosTocados(barco, estado)) {
                    barco.setHundido(true);
                    for (int[] pos : barco.getPositions()) {
                        estado[pos[0]][pos[1]] = -3;
                    }
                }
            } else {
                estado[r][c] = -1;
            }

            turnoFinal = t + 1;
            if (todosHundidos(barcos)) break;
        }

        boolean victoria = todosHundidos(barcos);
        return construirRawData(BOARD_SIZE, barcos, disparos, turnoFinal, victoria);
    }

    private List<Barco> colocarBarcos(List<Integer> tamanos, boolean[][] ocupado, Random rand) {
        List<Barco> resultado = new ArrayList<>();
        int id = 1;
        for (int tam : tamanos) {
            int intentos = 0;
            boolean colocado = false;
            while (!colocado && intentos < 500) {
                intentos++;
                boolean horizontal = rand.nextBoolean();
                int r = rand.nextInt(BOARD_SIZE);
                int c = rand.nextInt(BOARD_SIZE);

                List<int[]> celdas = new ArrayList<>();
                boolean valido = true;

                for (int k = 0; k < tam; k++) {
                    int nr = horizontal ? r : r + k;
                    int nc = horizontal ? c + k : c;
                    if (nr < 0 || nr >= BOARD_SIZE || nc < 0 || nc >= BOARD_SIZE || ocupado[nr][nc]) {
                        valido = false;
                        break;
                    }
                    celdas.add(new int[]{nr, nc});
                }

                if (valido) {
                    for (int[] pos : celdas) ocupado[pos[0]][pos[1]] = true;
                    resultado.add(new Barco(id++, tam, celdas));
                    colocado = true;
                }
            }
        }
        return resultado;
    }

    private List<int[]> generarDisparos(Random rand) {
        List<int[]> todas = new ArrayList<>();
        for (int r = 0; r < BOARD_SIZE; r++)
            for (int c = 0; c < BOARD_SIZE; c++)
                todas.add(new int[]{r, c});
        Collections.shuffle(todas, rand);
        return todas.subList(0, Math.min(DISPAROS_MAX, todas.size()));
    }

    private Barco buscarBarco(List<Barco> barcos, int r, int c) {
        for (Barco b : barcos)
            for (int[] pos : b.getPositions())
                if (pos[0] == r && pos[1] == c) return b;
        return null;
    }

    private boolean todosTocados(Barco barco, int[][] estado) {
        for (int[] pos : barco.getPositions())
            if (estado[pos[0]][pos[1]] != -2 && estado[pos[0]][pos[1]] != -3) return false;
        return true;
    }

    private boolean todosHundidos(List<Barco> barcos) {
        for (Barco b : barcos) if (!b.isHundido()) return false;
        return true;
    }

    private String construirRawData(int size, List<Barco> barcos, List<int[]> disparos,
                                    int numTurnos, boolean victoria) {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        sb.append(victoria ? "WIN" : "LOSE").append("\n");

        Map<String, Barco> mapeoPosicion = new HashMap<>();
        for (Barco b : barcos)
            for (int[] pos : b.getPositions())
                mapeoPosicion.put(pos[0] + "," + pos[1], b);

        for (int t = 0; t <= numTurnos; t++) {
            Set<String> fallos   = new HashSet<>();
            Set<String> tocados  = new HashSet<>();
            Set<String> hundidos = new HashSet<>();

            for (int d = 0; d < t && d < disparos.size(); d++) {
                int[] disp = disparos.get(d);
                String clave = disp[0] + "," + disp[1];
                Barco barco = mapeoPosicion.get(clave);
                if (barco == null) {
                    fallos.add(clave);
                } else {
                    tocados.add(clave);
                    boolean barcoCompleto = true;
                    for (int[] pos : barco.getPositions()) {
                        if (!tocados.contains(pos[0] + "," + pos[1])) {
                            barcoCompleto = false;
                            break;
                        }
                    }
                    if (barcoCompleto) {
                        for (int[] pos : barco.getPositions())
                            hundidos.add(pos[0] + "," + pos[1]);
                    }
                }
            }

            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    String clave = r + "," + c;
                    String color;
                    if (hundidos.contains(clave)) {
                        color = COLOR_HUNDIDO;
                    } else if (tocados.contains(clave)) {
                        color = COLOR_TOCADO;
                    } else if (fallos.contains(clave)) {
                        color = COLOR_FALLO;
                    } else if (mapeoPosicion.containsKey(clave)) {
                        color = colorBarco(mapeoPosicion.get(clave).getSize());
                    } else {
                        color = COLOR_AGUA;
                    }
                    sb.append(t).append(",").append(r).append(",").append(c)
                      .append(",").append(color).append("\n");
                }
            }
        }

        return sb.toString().trim();
    }

    private String colorBarco(int size) {
        if (size == 1) return COLOR_ALFA;
        if (size == 3) return COLOR_GAMMA;
        return COLOR_BETA;
    }
}
