package com.trabajo.servidor.service;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.model.DatosSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SimulacionServiceImplTest {

    // El servicio que vamos a probar
    private SimulacionServiceImpl simulacionService;

    // Esta anotación hace que este metodo se ejecute ANTES de cada test.
    // Nos garantiza que el "HashMap" esté vacío y limpio cada vez.
    @BeforeEach
    void setUp() {
        simulacionService = new SimulacionServiceImpl();
    }

    @Test
    void solicitarSimulacion_DebeDevolverUnTokenValidoYGuardarLosDatos() {
        // 1. Preparamos los datos de entrada
        DatosSolicitud solicitudFalsa = new DatosSolicitud();

        // 2. Ejecutamos el metodo
        String tokenGenerado = simulacionService.solicitarSimulacion(solicitudFalsa);

        // 3. Comprobamos resultados
        assertNotNull(tokenGenerado, "El token no debería ser nulo");
        assertFalse(tokenGenerado.isEmpty(), "El token no debería estar vacío");

        // Comprobamos que realmente se ha guardado en el mapa
        assertTrue(simulacionService.tokenEsValido(tokenGenerado), "El token debería existir en el almacenamiento");
    }

    @Test
    void obtenerSimulacion_ConTokenValido_DebeDevolverLaSimulacion() {
        // 1. Metemos una simulación primero para tener algo que buscar
        DatosSolicitud solicitud = new DatosSolicitud();
        String token = simulacionService.solicitarSimulacion(solicitud);

        // 2. Intentamos recuperarla
        Optional<DatosSimulacion> resultado = simulacionService.obtenerSimulacion(token);

        // 3. Comprobamos que la hemos encontrado y los datos coinciden
        assertTrue(resultado.isPresent(), "Debería encontrar la simulación");
        assertEquals(token, resultado.get().getToken(), "El token recuperado debe coincidir con el guardado");
        assertEquals("COMPLETADA", resultado.get().getEstado(), "El estado inicial debería ser COMPLETADA");
    }

    @Test
    void obtenerSimulacion_ConTokenInventado_DebeDevolverVacio() {
        // 1. Un token que sabemos que no existe
        String tokenFalso = "12345-token-inventado";

        // 2. Intentamos buscarlo
        Optional<DatosSimulacion> resultado = simulacionService.obtenerSimulacion(tokenFalso);

        // 3. Comprobamos que devuelve un Optional vacío
        assertTrue(resultado.isEmpty(), "No debería encontrar nada con un token falso");
    }

    @Test
    void obtenerTodas_DebeDevolverListaCompleta() {
        // 1. Guardamos 3 simulaciones
        simulacionService.solicitarSimulacion(new DatosSolicitud());
        simulacionService.solicitarSimulacion(new DatosSolicitud());
        simulacionService.solicitarSimulacion(new DatosSolicitud());

        // 2. Pedimos todas
        List<DatosSimulacion> lista = simulacionService.obtenerTodas();

        // 3. Comprobamos que la lista tiene tamaño 3
        assertEquals(3, lista.size(), "La lista debería contener exactamente 3 simulaciones");
    }

    @Test
    void obtenerTodas_CuandoNoHaySimulaciones_DebeRetornarListaVacia() {
        List<DatosSimulacion> lista = simulacionService.obtenerTodas();

        assertNotNull(lista, "La lista no debe ser null");
        assertTrue(lista.isEmpty(), "Sin simulaciones almacenadas la lista debe estar vacía");
    }

    @Test
    void tokenEsValido_ConTokenInexistente_DebeRetornarFalse() {
        boolean resultado = simulacionService.tokenEsValido("token-que-no-existe");

        assertFalse(resultado, "Un token no registrado debe ser inválido");
    }

    @Test
    void solicitarSimulacion_DebeGenerarTokenFormatoUUID() {
        String token = simulacionService.solicitarSimulacion(new DatosSolicitud());

        // Un UUID tiene 36 caracteres con el formato xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        assertNotNull(token);
        assertTrue(token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
                "El token debe tener formato UUID estándar");
    }

    @Test
    void solicitarSimulacion_DebeGuardarFechaDeCreacion() {
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        String token = simulacionService.solicitarSimulacion(new DatosSolicitud());
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        DatosSimulacion simulacion = simulacionService.obtenerSimulacion(token).orElseThrow();

        assertNotNull(simulacion.getFechaCreacion(), "La fecha de creación no debe ser null");
        assertTrue(simulacion.getFechaCreacion().isAfter(antes) && simulacion.getFechaCreacion().isBefore(despues),
                "La fecha de creación debe estar entre el inicio y el fin del test");
    }

    @Test
    void solicitarSimulacion_TokenesDistintosCadaVez() {
        String token1 = simulacionService.solicitarSimulacion(new DatosSolicitud());
        String token2 = simulacionService.solicitarSimulacion(new DatosSolicitud());

        assertNotEquals(token1, token2, "Dos solicitudes distintas deben generar tokens distintos");
    }

    @Test
    void solicitarSimulacion_GuardaElNombreCorrectamente() {
        DatosSolicitud solicitud = new DatosSolicitud("MiSimulacion", "Descripcion", 50);
        String token = simulacionService.solicitarSimulacion(solicitud);

        DatosSimulacion guardada = simulacionService.obtenerSimulacion(token).orElseThrow();

        assertEquals("MiSimulacion", guardada.getSolicitud().getNombreSimulacion());
        assertEquals(50, guardada.getSolicitud().getIteraciones());
    }
}
