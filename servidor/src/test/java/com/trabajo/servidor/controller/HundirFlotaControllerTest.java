package com.trabajo.servidor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabajo.servidor.model.BattleshipRequest;
import com.trabajo.servidor.service.HundirFlotaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HundirFlotaController.class)
class HundirFlotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HundirFlotaService hundirFlotaService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    // -----------------------------------------------------------------------
    // POST /Solicitud/Solicitar
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void solicitarSimulacion_DebeDevolverRespuestaConDoneYToken() throws Exception {
        Mockito.when(hundirFlotaService.generarToken()).thenReturn(7);

        BattleshipRequest request = buildRequest(2, 1, 0);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(7));
    }

    @Test
    @WithMockUser
    void solicitarSimulacion_ConParametroNombreUsuario_DebeFuncionar() throws Exception {
        Mockito.when(hundirFlotaService.generarToken()).thenReturn(3);

        BattleshipRequest request = buildRequest(0, 0, 1);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .param("nombreUsuario", "Marcos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(3));
    }

    @Test
    @WithMockUser
    void solicitarSimulacion_DebePublicarEnRabbitMQ() throws Exception {
        Mockito.when(hundirFlotaService.generarToken()).thenReturn(1);

        BattleshipRequest request = buildRequest(1, 1, 1);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(rabbitTemplate, Mockito.times(1))
                .convertAndSend(eq("simulacion.queue"), any(Object.class));
    }

    // -----------------------------------------------------------------------
    // POST /Resultados
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenValido_DebeDevolverLosDatos() throws Exception {
        String rawData = "10\nWIN\n0,0,0,#4488cc\n0,0,1,#22c55e";
        int tok = 42;
        Mockito.when(hundirFlotaService.obtenerRawData(tok)).thenReturn(rawData);

        mockMvc.perform(post("/Resultados")
                        .param("tok", String.valueOf(tok))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(tok))
                .andExpect(jsonPath("$.data").value(rawData));
    }

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenInvalido_DebeDevolverError() throws Exception {
        int invalidTok = 9999;
        Mockito.when(hundirFlotaService.obtenerRawData(invalidTok)).thenReturn(null);

        mockMvc.perform(post("/Resultados")
                        .param("tok", String.valueOf(invalidTok))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Token no encontrado: " + invalidTok));
    }

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenInvalido_NullData() throws Exception {
        int unknownTok = 0;
        Mockito.when(hundirFlotaService.obtenerRawData(unknownTok)).thenReturn(null);

        mockMvc.perform(post("/Resultados")
                        .param("tok", String.valueOf(unknownTok))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(false));
    }

    // -----------------------------------------------------------------------
    // Utilidad
    // -----------------------------------------------------------------------

    private BattleshipRequest buildRequest(int alfa, int beta, int gamma) {
        Map<Integer, Integer> nums = new HashMap<>();
        nums.put(1, alfa);
        nums.put(2, beta);
        nums.put(3, gamma);
        BattleshipRequest request = new BattleshipRequest();
        request.setNums(nums);
        return request;
    }
}
