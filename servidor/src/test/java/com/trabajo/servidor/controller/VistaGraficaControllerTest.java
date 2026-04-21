package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.model.DatosSolicitud;
import com.trabajo.servidor.service.SimulacionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de Integración Web para VistaGraficaController.
 */
@WebMvcTest(VistaGraficaController.class)
class VistaGraficaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulacionService simulacionService;

    @Test
    @WithMockUser
    void vistaGrafica_ConTokenValido_DebeCargarPaginaYModelo() throws Exception {
        // 1. Preparamos los datos REALES para evitar NullPointerException en la vista
        String tokenValido = "un-token-secreto-123";
        
        // Creamos una solicitud con datos
        DatosSolicitud solicitudFalsa = new DatosSolicitud("Simulacion de Prueba", "Escenario de test", 100);
        
        // Creamos la simulación completa
        DatosSimulacion simulacionFalsa = new DatosSimulacion(
                tokenValido, 
                solicitudFalsa, 
                "Resultado Mock", 
                "COMPLETADA", 
                LocalDateTime.now()
        );

        // Entrenamos al mock
        Mockito.when(simulacionService.obtenerSimulacion(tokenValido))
                .thenReturn(Optional.of(simulacionFalsa));

        // 2 & 3. Ejecutamos la petición
        mockMvc.perform(get("/vista-grafica/" + tokenValido))
                .andExpect(status().isOk())
                .andExpect(view().name("misionConseguida"))
                .andExpect(model().attributeExists("simulacion"))
                .andExpect(model().attributeExists("token"));
    }

    @Test
    @WithMockUser
    void vistaGrafica_ConTokenInvalido_DebeRedirigirADenegado() throws Exception {
        String tokenInvalido = "token-falso";
        Mockito.when(simulacionService.obtenerSimulacion(tokenInvalido)).thenReturn(Optional.empty());

        mockMvc.perform(get("/vista-grafica/" + tokenInvalido))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/acceso-denegado"));
    }

    @Test
    @WithMockUser
    void accesoDenegado_DebeCargarLaPaginaDeError() throws Exception {
        mockMvc.perform(get("/error/acceso-denegado"))
                .andExpect(status().isOk())
                .andExpect(view().name("accesoDenegado"));
    }
}
