package com.trabajo.servidor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HundirFlotaServiceImplTest {

    private HundirFlotaServiceImpl service;
    private Map<String, String> fakeStore;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        fakeStore = new HashMap<>();

        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        doAnswer(inv -> { fakeStore.put(inv.getArgument(0), inv.getArgument(1)); return null; })
                .when(valueOps).set(anyString(), anyString());
        when(valueOps.get(anyString())).thenAnswer(inv -> fakeStore.get(inv.getArgument(0)));

        service = new HundirFlotaServiceImpl(redisTemplate);
    }

    // -----------------------------------------------------------------------
    // generarToken
    // -----------------------------------------------------------------------

    @Test
    void generarToken_DebeDevolverId_Incremental() {
        int t1 = service.generarToken();
        int t2 = service.generarToken();
        assertTrue(t1 > 0, "El primer token debe ser positivo");
        assertEquals(t1 + 1, t2, "Los tokens deben ser incrementales");
    }

    // -----------------------------------------------------------------------
    // simularPartida
    // -----------------------------------------------------------------------

    @Test
    void simularPartida_DebeDevolverTokenPositivo() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 2);
        nums.put(2, 1);

        int token = service.simularPartida(nums);

        assertTrue(token > 0, "El token debe ser un entero positivo");
    }

    @Test
    void simularPartida_DebePersistirLosDatos() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token = service.simularPartida(nums);

        assertNotNull(service.obtenerRawData(token), "Los datos de la partida no deben ser null tras simular");
    }

    @Test
    void simularPartida_TokenesDebenSerUnicos() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token1 = service.simularPartida(nums);
        int token2 = service.simularPartida(nums);

        assertNotEquals(token1, token2, "Cada nueva partida debe recibir un token único");
    }

    @Test
    void simularPartida_VariasPartidasSonIndependientes() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token1 = service.simularPartida(nums);
        int token2 = service.simularPartida(nums);

        assertNotEquals(token1, token2);
        assertNotNull(service.obtenerRawData(token1));
        assertNotNull(service.obtenerRawData(token2));
    }

    @Test
    void simularPartida_SinBarcos_DebeUsarBarcosPorDefecto() {
        Map<Integer, Integer> nums = new HashMap<>();

        int token = service.simularPartida(nums);

        assertNotNull(service.obtenerRawData(token), "Incluso sin barcos debe generarse una partida");
    }

    // -----------------------------------------------------------------------
    // Formato del rawData
    // -----------------------------------------------------------------------

    @Test
    void rawData_PrimeraLineaDebeSerTamanoDelTablero() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(2, 2);

        int token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        assertEquals("21", rawData.split("\n")[0]);
    }

    @Test
    void rawData_SegundaLineaDebeSerWinOLose() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        String[] lineas = rawData.split("\n");
        assertTrue(lineas.length > 1);
        assertTrue(lineas[1].equals("WIN") || lineas[1].equals("LOSE"),
                "La segunda línea debe ser 'WIN' o 'LOSE', fue: " + lineas[1]);
    }

    @Test
    void rawData_LineasDeDatosDebenTenerCuatroColumnas() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        String[] lineas = rawData.split("\n");
        assertTrue(lineas.length > 2);
        assertEquals(4, lineas[2].split(",").length,
                "Cada línea de datos debe tener 4 campos: turno,fila,columna,color");
    }

    @Test
    void rawData_DebeContenerColorDeAgua() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        assertTrue(rawData.contains("#4488cc"), "El tablero debe contener celdas de agua (#4488cc)");
    }

    @Test
    void rawData_ConBarcos_DebeContenerColorDeBarco() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(3, 1);

        int token = service.simularPartida(nums);
        String rawData = service.obtenerRawData(token);

        assertNotNull(rawData);
        assertTrue(rawData.contains("#14b8a6"), "El tablero debe contener celdas de barco Gamma (#14b8a6)");
    }

    // -----------------------------------------------------------------------
    // obtenerRawData
    // -----------------------------------------------------------------------

    @Test
    void obtenerRawData_ConTokenInexistente_DebeRetornarNull() {
        assertNull(service.obtenerRawData(99999), "Un token que no existe debe devolver null");
    }
}
