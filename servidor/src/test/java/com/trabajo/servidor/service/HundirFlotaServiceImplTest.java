package com.trabajo.servidor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HundirFlotaServiceImplTest {

    private HundirFlotaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new HundirFlotaServiceImpl();
    }

    // -----------------------------------------------------------------------
    // simularPartida
    // -----------------------------------------------------------------------

    @Test
    void simularPartida_DebeDevolverTokenNoVacio() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 2);
        nums.put(2, 1);

        String token = service.simularPartida(nums);

        assertNotNull(token, "El token no debe ser null");
        assertFalse(token.isBlank(), "El token no debe estar vacío");
        assertEquals(64, token.length(), "El token debe tener 64 caracteres hexadecimales");
    }

    @Test
    void simularPartida_DebePersistirLosDatos() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token = service.simularPartida(nums);

        assertNotNull(service.obtenerRawData(token), "Los datos de la partida no deben ser null tras simular");
    }

    @Test
    void simularPartida_TokenesDebenSerUnicos() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token1 = service.simularPartida(nums);
        String token2 = service.simularPartida(nums);

        assertNotEquals(token1, token2, "Cada nueva partida debe recibir un token único");
    }

    @Test
    void simularPartida_VariasPartidasSonIndependientes() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token1 = service.simularPartida(nums);
        String token2 = service.simularPartida(nums);

        assertNotEquals(token1, token2, "Dos partidas distintas deben tener tokens distintos");
        assertNotNull(service.obtenerRawData(token1));
        assertNotNull(service.obtenerRawData(token2));
    }

    @Test
    void simularPartida_SinBarcos_DebeUsarBarcosPorDefecto() {
        // Map vacío → el servicio añade un barco de tamaño 2 por defecto
        Map<Integer, Integer> nums = new HashMap<>();

        String token = service.simularPartida(nums);

        assertNotNull(service.obtenerRawData(token), "Incluso sin barcos debe generarse una partida");
    }

    // -----------------------------------------------------------------------
    // Formato del rawData
    // -----------------------------------------------------------------------

    @Test
    void rawData_PrimeraLineaDebeSerTamanoDelTablero() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(2, 2);

        String token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        String primeraLinea = rawData.split("\n")[0];
        assertEquals("10", primeraLinea, "La primera línea del rawData debe ser '10' (tamaño del tablero)");
    }

    @Test
    void rawData_SegundaLineaDebeSerWinOLose() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        String[] lineas = rawData.split("\n");
        assertTrue(lineas.length > 1, "El rawData debe tener al menos 2 líneas de cabecera");
        assertTrue(lineas[1].equals("WIN") || lineas[1].equals("LOSE"),
                "La segunda línea debe ser 'WIN' o 'LOSE', fue: " + lineas[1]);
    }

    @Test
    void rawData_LineasDeDatosDebenTenerCuatroColumnas() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        String[] lineas = rawData.split("\n");
        // Línea 0 = tamaño, línea 1 = WIN/LOSE, línea 2 = primer dato
        assertTrue(lineas.length > 2, "El rawData debe tener datos tras las dos líneas de cabecera");

        String[] partes = lineas[2].split(",");
        assertEquals(4, partes.length, "Cada línea de datos debe tener 4 campos: turno,fila,columna,color");
    }

    @Test
    void rawData_DebeContenerColorDeAgua() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        String token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        assertTrue(rawData.contains("#4488cc"), "El tablero debe contener celdas de agua (#4488cc)");
    }

    @Test
    void rawData_ConBarcos_DebeContenerColorDeBarco() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(3, 1); // un crucero de tamaño 3

        String token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        // El turno 0 (estado inicial) debe mostrar los barcos
        assertTrue(rawData.contains("#808080"), "El tablero debe contener celdas de barco (#808080) en el turno inicial");
    }

    // -----------------------------------------------------------------------
    // obtenerRawData
    // -----------------------------------------------------------------------

    @Test
    void obtenerRawData_ConTokenInexistente_DebeRetornarNull() {
        String resultado = service.obtenerRawData("0000000000000000000000000000000000000000000000000000000000000000");

        assertNull(resultado, "Un token que no existe debe devolver null");
    }
}
