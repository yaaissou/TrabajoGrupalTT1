package com.trabajo.servidor.service;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.model.DatosSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
}
