package com.trabajo.servidor.service;

import java.util.Map;

public interface HundirFlotaService {

    int simularPartida(Map<Integer, Integer> nums);

    String obtenerRawData(int token);
}
