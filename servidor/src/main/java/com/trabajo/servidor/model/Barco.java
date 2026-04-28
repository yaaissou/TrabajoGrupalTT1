package com.trabajo.servidor.model;

import java.util.List;

public class Barco {

    private int id;
    private int size;
    private List<int[]> positions;
    private boolean hundido;

    public Barco(int id, int size, List<int[]> positions) {
        this.id = id;
        this.size = size;
        this.positions = positions;
        this.hundido = false;
    }

    public int getId() { return id; }
    public int getSize() { return size; }
    public List<int[]> getPositions() { return positions; }

    public boolean isHundido() { return hundido; }
    public void setHundido(boolean hundido) { this.hundido = hundido; }
}
