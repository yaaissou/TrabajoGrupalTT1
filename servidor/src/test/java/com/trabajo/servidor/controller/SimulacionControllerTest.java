package com.trabajo.servidor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabajo.servidor.model.DatosSolicitud;
import com.trabajo.servidor.service.SimulacionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulacionController.class)
class SimulacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimulacionService simulacionService;

    // ------------------------------------------------------------------------
    // TESTS PARA EL POST
    // ------------------------------------------------------------------------

    @Test
    @WithMockUser // Simula que un usuario autenticado está haciendo la petición
    void solicitarSimulacion_DebeDevolverTokenHttp200() throws Exception {
        DatosSolicitud solicitud = new DatosSolicitud();
        String tokenFalso = "token-12345-abcde";

        Mockito.when(simulacionService.solicitarSimulacion(any(DatosSolicitud.class)))
                .thenReturn(tokenFalso);

        mockMvc.perform(post("/api/simulacion/solicitar")
                        .with(csrf()) // ¡VITAL! Simula el token de seguridad para que el POST no sea rechazado (Error 403)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))

                .andExpect(status().isOk())
                .andExpect(content().string(tokenFalso));
    }

    // -----------------------------------------------------------------------
    // TESTS PARA EL GET
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser // Simula usuario autenticado
    void accederConToken_SiElTokenEsValido_DebeRedirigirAVistaGrafica() throws Exception {
        String tokenValido = "un-token-que-si-existe";

        Mockito.when(simulacionService.tokenEsValido(tokenValido)).thenReturn(true);

        mockMvc.perform(get("/api/simulacion/acceder/" + tokenValido))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vista-grafica/" + tokenValido));
    }

    @Test
    @WithMockUser // Simula usuario autenticado
    void accederConToken_SiElTokenEsInvalido_DebeRedirigirAError() throws Exception {
        String tokenInvalido = "token-inventado";

        Mockito.when(simulacionService.tokenEsValido(tokenInvalido)).thenReturn(false);

        mockMvc.perform(get("/api/simulacion/acceder/" + tokenInvalido))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/acceso-denegado"));
    }
}
