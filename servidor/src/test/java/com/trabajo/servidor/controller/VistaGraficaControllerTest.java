package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.service.SimulacionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de Integración Web para VistaGraficaController.
 * Aquí probamos que las plantillas Thymeleaf se llamen correctamente
 * y se pasen los datos a la vista.
 */
@WebMvcTest(VistaGraficaController.class)
class VistaGraficaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulacionService simulacionService;

    // ------------------------------------------------------------------------
    // TESTS PARA: GET /vista-grafica/{token}
    // ------------------------------------------------------------------------

    @Test
    @WithMockUser // Saltamos la seguridad de Spring Security
    void vistaGrafica_ConTokenValido_DebeCargarPaginaYModelo() throws Exception {
        // 1. Preparamos los datos
        String tokenValido = "un-token-secreto-123";
        DatosSimulacion simulacionFalsa = new DatosSimulacion(); // Creamos un objeto falso

        // Entrenamos al mock: cuando busquen este token, devuelve la simulación falsa
        Mockito.when(simulacionService.obtenerSimulacion(tokenValido))
                .thenReturn(Optional.of(simulacionFalsa));

        // 2 & 3. Lanzamos la petición GET
        mockMvc.perform(get("/vista-grafica/" + tokenValido))

                // ¿Qué esperamos que pase?
                .andExpect(status().isOk()) // Esperamos un HTTP 200 (Página cargada con éxito)

                // ¡NUEVO! Comprobamos que el controlador llama al archivo HTML correcto ("misionConseguida.html")
                .andExpect(view().name("misionConseguida"))

                // ¡NUEVO! Comprobamos que el controlador ha inyectado los datos para Thymeleaf
                .andExpect(model().attributeExists("simulacion"))
                .andExpect(model().attributeExists("token"));
    }

    @Test
    @WithMockUser
    void vistaGrafica_ConTokenInvalido_DebeRedirigirADenegado() throws Exception {
        // 1.
        String tokenInvalido = "token-falso";

        // Entrenamos al mock: devuelve un Optional VACÍO (simulando que no encontró nada)
        Mockito.when(simulacionService.obtenerSimulacion(tokenInvalido))
                .thenReturn(Optional.empty());

        // 2 & 3.
        mockMvc.perform(get("/vista-grafica/" + tokenInvalido))

                // Como el código tiene un "return redirect:...", esperamos un HTTP 302 (Redirección)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/acceso-denegado")); // Y esperamos que nos mande aquí
    }

    // ------------------------------------------------------------------------
    // TESTS PARA: GET /error/acceso-denegado
    // ------------------------------------------------------------------------

    @Test
    @WithMockUser
    void accesoDenegado_DebeCargarLaPaginaDeError() throws Exception {
        // 1 & 2 & 3. 
        // Esta ruta es súper sencilla, no usa servicios, solo devuelve un HTML.
        mockMvc.perform(get("/error/acceso-denegado"))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(view().name("accesoDenegado")); // Llama al archivo "accesoDenegado.html"
    }
}
