package com.trabajo.servidor.model;

/**
 * Entidad de configuración de entrada que el cliente envía al servidor
 * para solicitar la creación de un nuevo escenario.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
public class DatosSolicitud {

    /** Nombre descriptivo que el usuario asigna a la solicitud. */
    private String nombreSimulacion;

    /** Explicación detallada y opcional sobre el escenario particular. */
    private String descripcion;

    /** Número total de ciclos o iteraciones que el motor debe ejecutar. */
    private int iteraciones;

    /**
     * Constructor por defecto, requerido por frameworks de serialización (Jackson/Spring).
     */
    public DatosSolicitud() {}

    /**
     * Inicializa una nueva solicitud de simulación con todos sus parámetros.
     *
     * @param nombreSimulacion Título de la simulación.
     * @param descripcion      Contexto textual explicativo.
     * @param iteraciones      Cantidad de ciclos matemáticos a procesar.
     */
    public DatosSolicitud(String nombreSimulacion, String descripcion, int iteraciones) {
        this.nombreSimulacion = nombreSimulacion;
        this.descripcion = descripcion;
        this.iteraciones = iteraciones;
    }

    /** @return El nombre asociado a la simulación. */
    public String getNombreSimulacion() { return nombreSimulacion; }

    /** @param nombreSimulacion El título a establecer. */
    public void setNombreSimulacion(String nombreSimulacion) { this.nombreSimulacion = nombreSimulacion; }

    /** @return El texto descriptivo del escenario. */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion La nueva descripción a guardar. */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return El número de iteraciones o pasos configurados. */
    public int getIteraciones() { return iteraciones; }

    /** @param iteraciones La cantidad entera de ciclos de ejecución. */
    public void setIteraciones(int iteraciones) { this.iteraciones = iteraciones; }
}