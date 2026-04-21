package com.trabajo.servidor.model;

/**
 * Datos de configuración que el cliente envía al servidor
 * para solicitar la creación de una nueva simulación.
 */
public class DatosSolicitud {

    /** * Nombre descriptivo que el usuario asigna a la simulación.
     */
    private String nombreSimulacion;

    /** * Descripción detallada y opcional sobre el escenario a simular.
     */
    private String descripcion;

    /** * Número total de iteraciones que el motor de simulación debe ejecutar.
     */
    private int iteraciones;

    // --- CONSTRUCTORES ---
    public DatosSolicitud() {}

    public DatosSolicitud(String nombreSimulacion, String descripcion, int iteraciones) {
        this.nombreSimulacion = nombreSimulacion;
        this.descripcion = descripcion;
        this.iteraciones = iteraciones;
    }

    // --- GETTERS Y SETTERS ---
    public String getNombreSimulacion() { return nombreSimulacion; }
    public void setNombreSimulacion(String nombreSimulacion) { this.nombreSimulacion = nombreSimulacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIteraciones() { return iteraciones; }
    public void setIteraciones(int iteraciones) { this.iteraciones = iteraciones; }
}