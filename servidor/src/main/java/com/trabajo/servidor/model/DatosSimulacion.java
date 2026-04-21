package com.trabajo.servidor.model;

import java.time.LocalDateTime;

/**
 * Representa el resultado de una simulación generada por el servidor.
 * <p>
 * Esta clase es el <strong>VALOR</strong> que se almacena en el {@code ConcurrentHashMap} de la capa de servicio.
 * La <strong>CLAVE</strong> del mapa corresponde al campo {@code token}, que actúa como
 * identificador único y seguro de acceso para el usuario.
 * @version 1.0
 */
public class DatosSimulacion {

    /**
     * Token UUID único que identifica esta simulación.
     * <p>
     * El usuario lo utiliza como "llave" en la URL para poder acceder a la
     * interfaz gráfica y ver sus resultados.
     */
    private String token;

    /** * Objeto que contiene los parámetros de configuración originales
     * que el usuario envió en la petición inicial.
     */
    private DatosSolicitud solicitud;

    /** * Texto con el resultado del cálculo generado por el servidor
     * (actualmente devuelve datos mock para esta fase del desarrollo).
     */
    private String resultado;

    /**
     * Estado actual del ciclo de vida de la simulación.
     * <p>
     * Valores posibles:
     * <ul>
     * <li>{@code PENDIENTE}</li>
     * <li>{@code EN_PROCESO}</li>
     * <li>{@code COMPLETADA}</li>
     * <li>{@code ERROR}</li>
     * </ul>
     */
    private String estado;

    /** * Fecha y hora exacta (marca temporal) en la que se generó
     * y registró la simulación en el sistema.
     */
    private LocalDateTime fechaCreacion;

    // --- CONSTRUCTORES ---
    public DatosSimulacion() {}

    public DatosSimulacion(String token, DatosSolicitud solicitud, String resultado, String estado, LocalDateTime fechaCreacion) {
        this.token = token;
        this.solicitud = solicitud;
        this.resultado = resultado;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // --- GETTERS Y SETTERS ---
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public DatosSolicitud getSolicitud() { return solicitud; }
    public void setSolicitud(DatosSolicitud solicitud) { this.solicitud = solicitud; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}