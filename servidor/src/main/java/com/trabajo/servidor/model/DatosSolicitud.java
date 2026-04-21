package com.trabajo.servidor.model;

/**
 * Datos de configuración que el usuario envía al servidor
 * para solicitar una nueva simulación.
 *
 * El servidor los recibe en el body del POST /api/simulacion/solicitar.
 */
public class DatosSolicitud {

    private String nombreSimulacion;
    private String descripcion;
    private int iteraciones;

    // Constructor vacío
    public DatosSolicitud() {
    }

    // Constructor con todos los parámetros
    public DatosSolicitud(String nombreSimulacion, String descripcion, int iteraciones) {
        this.nombreSimulacion = nombreSimulacion;
        this.descripcion = descripcion;
        this.iteraciones = iteraciones;
    }

    // --- Getters y Setters explícitos (Adiós problemas de Lombok) ---

    public String getNombreSimulacion() {
        return nombreSimulacion;
    }

    public void setNombreSimulacion(String nombreSimulacion) {
        this.nombreSimulacion = nombreSimulacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIteraciones() {
        return iteraciones;
    }

    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }
}