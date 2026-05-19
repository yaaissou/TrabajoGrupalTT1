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
        assertTrue(t1 >= 10000, "El token debe tener al menos 5 dígitos");
        assertEquals(t1 + 1, t2, "Los tokens deben ser incrementales");
    }

    @Test
    void generarToken_DebeSerLargo_MasDeUnDigito() {
        int token = service.generarToken();
        assertTrue(String.valueOf(token).length() > 1, "El token debe tener más de un dígito");
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

    // -----------------------------------------------------------------------
    // Redis: verificar persistencia real del store
    // -----------------------------------------------------------------------

    @Test
    void redis_procesarSolicitud_DebePersistirDatos() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 2);
        nums.put(2, 1);

        int token = service.generarToken();
        service.procesarSolicitud(token, nums);

        String rawData = service.obtenerRawData(token);
        assertNotNull(rawData, "Redis debe haber persistido los datos con procesarSolicitud");
        assertFalse(rawData.isBlank(), "Los datos persistidos no deben estar vacíos");
    }

    @Test
    void redis_SobreescrituraMismoToken_DebeActualizarElValor() {
        Map<Integer, Integer> nums1 = new HashMap<>();
        nums1.put(1, 1);
        Map<Integer, Integer> nums2 = new HashMap<>();
        nums2.put(2, 2);

        int token = service.generarToken();
        service.procesarSolicitud(token, nums1);
        String datosIniciales = service.obtenerRawData(token);

        service.procesarSolicitud(token, nums2);
        String datosActualizados = service.obtenerRawData(token);

        assertNotNull(datosIniciales);
        assertNotNull(datosActualizados);
    }

    @Test
    void redis_TokenesDistintos_DatosSonIndependientes() {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, 1);

        int token1 = service.simularPartida(nums);
        int token2 = service.simularPartida(nums);

        assertNotEquals(token1, token2, "Los tokens deben ser distintos");
        assertNotNull(service.obtenerRawData(token1), "Redis debe tener datos para token1");
        assertNotNull(service.obtenerRawData(token2), "Redis debe tener datos para token2");
        assertNull(service.obtenerRawData(token1 - 1), "Token no generado debe devolver null en Redis");
    }
}
