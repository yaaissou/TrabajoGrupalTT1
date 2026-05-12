package com.trabajo.servidor.service;

import com.trabajo.servidor.model.Barco;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HundirFlotaServiceImpl implements HundirFlotaService {

    // Tablero cuadrado 21x21
    // Lado A (jugador A): filas 0-20, cols 0-9
    // Muralla:            filas 0-20, col  10
    // Lado B (jugador B): filas 0-20, cols 11-20
    private static final int BOARD_SIZE  = 21;
    private static final int SIDE_ROWS   = 21;
    private static final int SIDE_COLS   = 10;
    private static final int WALL_COL    = 10;
    private static final int A_COL_START = 0;
    private static final int A_COL_END   = 9;
    private static final int B_COL_START = 11;
    private static final int B_COL_END   = 20;

    private static final String COLOR_AGUA    = "#4488cc";
    private static final String COLOR_ALFA    = "#22c55e";
    private static final String COLOR_BETA    = "#20d06d";
    private static final String COLOR_GAMMA   = "#14b8a6";
    private static final String COLOR_FALLO   = "#99ccff";
    private static final String COLOR_TOCADO  = "orange";
    private static final String COLOR_HUNDIDO = "#cc0000";
    private static final String COLOR_MURALLA = "#222222";

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

    // -------------------------------------------------------------------------
    // Simulación 1 vs 1
    // -------------------------------------------------------------------------

    private String ejecutarSimulacion(Map<Integer, Integer> nums) {
        int nAlfa  = nums.getOrDefault(1, 0);
        int nBeta  = nums.getOrDefault(2, 0);
        int nGamma = nums.getOrDefault(3, 0);

        int maxCeldas = SIDE_ROWS * SIDE_COLS / 2;
        List<Integer> tamanos = new ArrayList<>();
        int total = 0;
        for (int i = 0; i < nAlfa  && total + 1 <= maxCeldas; i++) { tamanos.add(1); total += 1; }
        for (int i = 0; i < nBeta  && total + 2 <= maxCeldas; i++) { tamanos.add(2); total += 2; }
        for (int i = 0; i < nGamma && total + 3 <= maxCeldas; i++) { tamanos.add(3); total += 3; }
        if (tamanos.isEmpty()) tamanos.add(2);

        Random rand = new Random();

        // Colocar los mismos tipos de barcos en cada lado (posiciones aleatorias)
        List<Barco> barcosA = colocarBarcos(tamanos, rand, 0, SIDE_ROWS - 1, A_COL_START, A_COL_END);
        List<Barco> barcosB = colocarBarcos(tamanos, rand, 0, SIDE_ROWS - 1, B_COL_START, B_COL_END);

        // Estado de celdas: 0=agua 1=barco -1=fallo -2=tocado -3=hundido
        // Índice local: estadoX[fila][col - colOffset]
        int[][] estadoA = new int[SIDE_ROWS][SIDE_COLS];
        int[][] estadoB = new int[SIDE_ROWS][SIDE_COLS];

        for (Barco b : barcosA)
            for (int[] p : b.getPositions())
                estadoA[p[0]][p[1] - A_COL_START] = 1;

        for (Barco b : barcosB)
            for (int[] p : b.getPositions())
                estadoB[p[0]][p[1] - B_COL_START] = 1;

        // Mapa coords absolutas → barco (para buscar impactos)
        Map<String, Barco> mapeoA = buildMapeo(barcosA);
        Map<String, Barco> mapeoB = buildMapeo(barcosB);

        // Mapas de color de barco para los snapshots
        Map<String, String> colorMapA = buildColorMap(barcosA);
        Map<String, String> colorMapB = buildColorMap(barcosB);

        // Disparadores inteligentes
        // A dispara al lado B; B dispara al lado A
        IntelligentShooter shooterA = new IntelligentShooter(0, SIDE_ROWS - 1, B_COL_START, B_COL_END, rand);
        IntelligentShooter shooterB = new IntelligentShooter(0, SIDE_ROWS - 1, A_COL_START, A_COL_END, rand);

        List<String> snaps = new ArrayList<>();
        snaps.add(buildSnapshot(0, estadoA, colorMapA, estadoB, colorMapB));

        boolean aGana = false;
        int turno = 0;
        int maxTurnos = SIDE_ROWS * SIDE_COLS * 2; // límite de seguridad

        bucle:
        for (int i = 0; i < maxTurnos; i++) {
            // --- Turno de A ---
            int[] shotA = shooterA.nextShot();
            procesarDisparo(shotA[0], shotA[1], barcosB, estadoB, B_COL_START, mapeoB, shooterA);
            turno++;
            snaps.add(buildSnapshot(turno, estadoA, colorMapA, estadoB, colorMapB));
            if (todosHundidos(barcosB)) { aGana = true; break bucle; }

            // --- Turno de B ---
            int[] shotB = shooterB.nextShot();
            procesarDisparo(shotB[0], shotB[1], barcosA, estadoA, A_COL_START, mapeoA, shooterB);
            turno++;
            snaps.add(buildSnapshot(turno, estadoA, colorMapA, estadoB, colorMapB));
            if (todosHundidos(barcosA)) { aGana = false; break bucle; }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(BOARD_SIZE).append("\n");
        sb.append(aGana ? "WIN" : "LOSE").append("\n");
        for (String snap : snaps) sb.append(snap);

        return sb.toString().trim();
    }

    // -------------------------------------------------------------------------
    // Helpers de simulación
    // -------------------------------------------------------------------------

    private void procesarDisparo(int r, int c,
                                  List<Barco> barcos, int[][] estado, int colOffset,
                                  Map<String, Barco> mapeo, IntelligentShooter shooter) {
        Barco barco = mapeo.get(r + "," + c);
        if (barco != null) {
            estado[r][c - colOffset] = -2;
            if (todosTocados(barco, estado, colOffset)) {
                barco.setHundido(true);
                for (int[] pos : barco.getPositions())
                    estado[pos[0]][pos[1] - colOffset] = -3;
                shooter.recordSunk(r, c);
            } else {
                shooter.recordHit(r, c);
            }
        } else {
            estado[r][c - colOffset] = -1;
            shooter.recordMiss(r, c);
        }
    }

    private boolean todosTocados(Barco barco, int[][] estado, int colOffset) {
        for (int[] pos : barco.getPositions()) {
            int e = estado[pos[0]][pos[1] - colOffset];
            if (e != -2 && e != -3) return false;
        }
        return true;
    }

    private boolean todosHundidos(List<Barco> barcos) {
        for (Barco b : barcos) if (!b.isHundido()) return false;
        return true;
    }

    // -------------------------------------------------------------------------
    // Construcción de snapshots
    // -------------------------------------------------------------------------

    private String buildSnapshot(int turno,
                                  int[][] estadoA, Map<String, String> colorMapA,
                                  int[][] estadoB, Map<String, String> colorMapB) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < SIDE_ROWS; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                String color;
                if (c == WALL_COL) {
                    color = COLOR_MURALLA;
                } else if (c <= A_COL_END) {
                    color = colorDesdEstado(estadoA[r][c], colorMapA.get(r + "," + c));
                } else {
                    color = colorDesdEstado(estadoB[r][c - B_COL_START], colorMapB.get(r + "," + c));
                }
                sb.append(turno).append(",").append(r).append(",").append(c)
                  .append(",").append(color).append("\n");
            }
        }
        return sb.toString();
    }

    private String colorDesdEstado(int estado, String colorBarco) {
        return switch (estado) {
            case  1 -> colorBarco != null ? colorBarco : COLOR_ALFA;
            case -1 -> COLOR_FALLO;
            case -2 -> COLOR_TOCADO;
            case -3 -> COLOR_HUNDIDO;
            default -> COLOR_AGUA;
        };
    }

    // -------------------------------------------------------------------------
    // Colocación de barcos
    // -------------------------------------------------------------------------

    private List<Barco> colocarBarcos(List<Integer> tamanos, Random rand,
                                       int rowMin, int rowMax, int colMin, int colMax) {
        List<Barco> resultado = new ArrayList<>();
        int sideRows = rowMax - rowMin + 1;
        int sideCols = colMax - colMin + 1;
        boolean[][] ocupado = new boolean[sideRows][sideCols];
        int id = 1;

        for (int tam : tamanos) {
            int intentos = 0;
            boolean colocado = false;
            while (!colocado && intentos < 500) {
                intentos++;
                boolean horizontal = rand.nextBoolean();
                int r = rowMin + rand.nextInt(sideRows);
                int c = colMin + rand.nextInt(sideCols);

                List<int[]> celdas = new ArrayList<>();
                boolean valido = true;
                for (int k = 0; k < tam; k++) {
                    int nr = horizontal ? r     : r + k;
                    int nc = horizontal ? c + k : c;
                    if (nr < rowMin || nr > rowMax || nc < colMin || nc > colMax
                            || ocupado[nr - rowMin][nc - colMin]) {
                        valido = false;
                        break;
                    }
                    celdas.add(new int[]{nr, nc});
                }
                if (valido) {
                    for (int[] pos : celdas) ocupado[pos[0] - rowMin][pos[1] - colMin] = true;
                    resultado.add(new Barco(id++, tam, celdas));
                    colocado = true;
                }
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Mapeos auxiliares
    // -------------------------------------------------------------------------

    private Map<String, Barco> buildMapeo(List<Barco> barcos) {
        Map<String, Barco> m = new HashMap<>();
        for (Barco b : barcos)
            for (int[] pos : b.getPositions())
                m.put(pos[0] + "," + pos[1], b);
        return m;
    }

    private Map<String, String> buildColorMap(List<Barco> barcos) {
        Map<String, String> m = new HashMap<>();
        for (Barco b : barcos) {
            String color = colorBarco(b.getSize());
            for (int[] pos : b.getPositions())
                m.put(pos[0] + "," + pos[1], color);
        }
        return m;
    }

    private String colorBarco(int size) {
        if (size == 1) return COLOR_ALFA;
        if (size == 3) return COLOR_GAMMA;
        return COLOR_BETA;
    }

    // -------------------------------------------------------------------------
    // Disparador inteligente (Hunt & Target)
    // -------------------------------------------------------------------------

    private static class IntelligentShooter {

        private final int rowMin, rowMax, colMin, colMax;
        private final Random rand;
        private final Set<String> shotAt    = new HashSet<>();
        private final Deque<int[]> targets  = new ArrayDeque<>();

        IntelligentShooter(int rowMin, int rowMax, int colMin, int colMax, Random rand) {
            this.rowMin = rowMin; this.rowMax = rowMax;
            this.colMin = colMin; this.colMax = colMax;
            this.rand   = rand;
        }

        int[] nextShot() {
            // Descartar objetivos ya disparados
            while (!targets.isEmpty() && shotAt.contains(key(targets.peekFirst())))
                targets.pollFirst();

            if (!targets.isEmpty()) return targets.pollFirst();

            // Modo caza: celda aleatoria no disparada
            List<int[]> disponibles = new ArrayList<>();
            for (int r = rowMin; r <= rowMax; r++)
                for (int c = colMin; c <= colMax; c++)
                    if (!shotAt.contains(key(r, c))) disponibles.add(new int[]{r, c});

            return disponibles.get(rand.nextInt(disponibles.size()));
        }

        // Tocado pero no hundido → añadir celdas adyacentes como objetivos
        void recordHit(int r, int c) {
            shotAt.add(key(r, c));
            int[][] adj = {{r - 1, c}, {r + 1, c}, {r, c - 1}, {r, c + 1}};
            for (int[] a : adj) {
                if (a[0] >= rowMin && a[0] <= rowMax
                        && a[1] >= colMin && a[1] <= colMax
                        && !shotAt.contains(key(a[0], a[1]))) {
                    targets.addLast(a);
                }
            }
        }

        // Hundido → limpiar cola y volver a modo caza
        void recordSunk(int r, int c) {
            shotAt.add(key(r, c));
            targets.clear();
        }

        void recordMiss(int r, int c) {
            shotAt.add(key(r, c));
        }

        private String key(int[] pos) { return pos[0] + "," + pos[1]; }
        private String key(int r, int c) { return r + "," + c; }
    }
}
