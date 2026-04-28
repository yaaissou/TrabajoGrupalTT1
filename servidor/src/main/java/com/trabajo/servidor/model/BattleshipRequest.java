package com.trabajo.servidor.model;

import java.util.Map;

public class BattleshipRequest {

    private Map<Integer, Integer> nums;

    public Map<Integer, Integer> getNums() { return nums; }
    public void setNums(Map<Integer, Integer> nums) { this.nums = nums; }
}
