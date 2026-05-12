package com.trabajo.servidor.controller;

import com.trabajo.servidor.service.HundirFlotaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JuegoController.class)
class JuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HundirFlotaService hundirFlotaService;

    // -----------------------------------------------------------------------
    // GET /juego/token
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void mostrarToken_DebeCargarVistaCorrecta() throws Exception {
        mockMvc.perform(get("/juego/token").param("tok", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("juego/token"));
    }

    @Test
    @WithMockUser
    void mostrarToken_DebeInyectarTokenEnModelo() throws Exception {
        mockMvc.perform(get("/juego/token").param("tok", "3"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", 3));
    }

    @Test
    @WithMockUser
    void mostrarToken_ConDistintosTokens_DebeInyectarElCorrecto() throws Exception {
        mockMvc.perform(get("/juego/token").param("tok", "99"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", 99));
    }

    // -----------------------------------------------------------------------
    // GET /juego/partida
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser
    void mostrarPartida_ConTokenValido_DebeCargarVistaConDatos() throws Exception {
        String rawData = "10\nLOSE\n0,0,0,#4488cc\n0,0,1,#808080";
        Mockito.when(hundirFlotaService.obtenerRawData(1)).thenReturn(rawData);

        mockMvc.perform(get("/juego/partida").param("tok", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("juego/partida"))
                .andExpect(model().attribute("tok", 1))
                .andExpect(model().attribute("datosGrid", rawData));
    }

    @Test
    @WithMockUser
    void mostrarPartida_ConTokenInvalido_DebeCargarVistaDeError() throws Exception {
        Mockito.when(hundirFlotaService.obtenerRawData(999)).thenReturn(null);

        mockMvc.perform(get("/juego/partida").param("tok", "999"))
                .andExpect(status().isOk())
                .andExpect(view().name("juego/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser
    void mostrarPartida_ConTokenInvalido_MensajeErrorContieneElToken() throws Exception {
        Mockito.when(hundirFlotaService.obtenerRawData(42)).thenReturn(null);

        mockMvc.perform(get("/juego/partida").param("tok", "42"))
                .andExpect(status().isOk())
                .andExpect(view().name("juego/error"))
                .andExpect(model().attribute("error", "Token 42 no encontrado."));
    }
}
