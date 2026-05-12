package com.trabajo.servidor.service;

import java.util.Map;

public interface HundirFlotaService {

    int generarToken();

    int simularPartida(Map<Integer, Integer> nums);

    void procesarSolicitud(int token, Map<Integer, Integer> nums);

    String obtenerRawData(int token);
}
