package com.trabajo.servidor.model;

import java.util.List;

/**
 * Representa una entidad física (un navío) dentro del tablero de Hundir la Flota.
 * <p>
 * Almacena el estado interno del barco, su longitud y las coordenadas exactas
 * que ocupa en la cuadrícula de simulación.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
public class Barco {

    /** Identificador único del barco dentro de la partida. */
    private int id;

    /** Tamaño del barco (número de celdas que ocupa). */
    private int size;

    /** * Lista de coordenadas espaciales que ocupa el barco.
     * Cada elemento de la lista es un array de enteros, típicamente en formato [fila, columna].
     */
    private List<int[]> positions;

    /** Estado vital del barco. Será {@code true} si todas sus celdas han sido impactadas. */
    private boolean hundido;

    /**
     * Construye un nuevo barco inicializando sus parámetros físicos.
     * Por defecto, un barco recién creado se inicializa como no hundido.
     *
     * @param id        El identificador numérico único.
     * @param size      La longitud o cantidad de celdas del barco.
     * @param positions Las coordenadas de la cuadrícula donde se posiciona.
     */
    public Barco(int id, int size, List<int[]> positions) {
        this.id = id;
        this.size = size;
        this.positions = positions;
        this.hundido = false;
    }

    /**
     * @return El identificador numérico del barco.
     */
    public int getId() { return id; }

    /**
     * @return El tamaño total del barco en celdas.
     */
    public int getSize() { return size; }

    /**
     * @return Las coordenadas geográficas [x, y] que ocupa en el tablero.
     */
    public List<int[]> getPositions() { return positions; }

    /**
     * @return {@code true} si el barco está destruido por completo, {@code false} en caso contrario.
     */
    public boolean isHundido() { return hundido; }

    /**
     * Actualiza el estado vital del barco.
     *
     * @param hundido El nuevo estado a establecer.
     */
    public void setHundido(boolean hundido) { this.hundido = hundido; }
}