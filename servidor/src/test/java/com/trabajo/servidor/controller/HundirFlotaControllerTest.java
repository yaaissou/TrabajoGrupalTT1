package com.trabajo.servidor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabajo.servidor.model.BattleshipRequest;
import com.trabajo.servidor.service.HundirFlotaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
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

    // -----------------------------------------------------------------------
    // POST /Solicitud/Solicitar
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void solicitarSimulacion_DebeDevolverRespuestaConDoneYToken() throws Exception {
        String fakeToken = "a3f8b2d1e9c047f6b5a2d8e1f3c7b9a4e6f2d8c1b7a3e9f5c2d6b4a8e0f7c3d1";
        Mockito.when(hundirFlotaService.simularPartida(any())).thenReturn(fakeToken);

        BattleshipRequest request = buildRequest(2, 1, 0);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(fakeToken));
    }

    @Test
    @WithMockUser
    void solicitarSimulacion_ConParametroNombreUsuario_DebeFuncionar() throws Exception {
        String fakeToken = "c9e2a7f4b1d30e8f6c5a2b9d7e4f1a8c3d6b0e5f2a9c7d4b1e8f3a6c0d2b5e7f4";
        Mockito.when(hundirFlotaService.simularPartida(any())).thenReturn(fakeToken);

        BattleshipRequest request = buildRequest(0, 0, 1);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .param("nombreUsuario", "Marcos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(fakeToken));
    }

    @Test
    @WithMockUser
    void solicitarSimulacion_DebeInvocarAlServicio() throws Exception {
        Mockito.when(hundirFlotaService.simularPartida(any())).thenReturn("b4d1a8e3f6c2b9d5e1a7f4c0b8d3e6a2f5c9b1d7e4a0f3c6b2d8e5a1f7c4b0d9e6");

        BattleshipRequest request = buildRequest(1, 1, 1);

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(hundirFlotaService, Mockito.times(1)).simularPartida(any());
    }

    // -----------------------------------------------------------------------
    // POST /Resultados
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenValido_DebeDevolverLosDatos() throws Exception {
        String rawData = "10\nWIN\n0,0,0,#4488cc\n0,0,1,#808080";
        String tok = "d7e2f5a9c1b4d8e3f6a0c5b2d9e4f1a7c3b6d0e8f2a5c9b1d4e7f3a0c6b8d2e5f1";
        Mockito.when(hundirFlotaService.obtenerRawData(tok)).thenReturn(rawData);

        mockMvc.perform(post("/Resultados")
                        .param("tok", tok)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(tok))
                .andExpect(jsonPath("$.data").value(rawData));
    }

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenInvalido_DebeDevolverError() throws Exception {
        String invalidTok = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        Mockito.when(hundirFlotaService.obtenerRawData(invalidTok)).thenReturn(null);

        mockMvc.perform(post("/Resultados")
                        .param("tok", invalidTok)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Token no encontrado: " + invalidTok));
    }

    @Test
    @WithMockUser
    void obtenerResultados_ConTokenInvalido_NullData() throws Exception {
        String unknownTok = "0000000000000000000000000000000000000000000000000000000000000000";
        Mockito.when(hundirFlotaService.obtenerRawData(unknownTok)).thenReturn(null);

        mockMvc.perform(post("/Resultados")
                        .param("tok", unknownTok)
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
