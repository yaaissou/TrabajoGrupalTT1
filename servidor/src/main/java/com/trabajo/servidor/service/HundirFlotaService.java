package com.trabajo.servidor.service;

import java.util.Map;

public interface HundirFlotaService {

    String simularPartida(Map<Integer, Integer> nums);

    String obtenerRawData(String token);
}
