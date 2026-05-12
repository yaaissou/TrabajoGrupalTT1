package com.trabajo.servidor.model;

import java.util.Map;

public class SimulacionMensaje {

    private int token;
    private Map<Integer, Integer> nums;

    public SimulacionMensaje() {}

    public SimulacionMensaje(int token, Map<Integer, Integer> nums) {
        this.token = token;
        this.nums = nums;
    }

    public int getToken() { return token; }
    public void setToken(int token) { this.token = token; }

    public Map<Integer, Integer> getNums() { return nums; }
    public void setNums(Map<Integer, Integer> nums) { this.nums = nums; }
}
