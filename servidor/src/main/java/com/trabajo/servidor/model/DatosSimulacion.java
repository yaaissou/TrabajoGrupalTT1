package com.trabajo.servidor.model;

import java.time.LocalDateTime;

/**
 * Resultado de una simulación generada por el servidor.
 *
 * Esta clase es el VALOR que se almacena en el ConcurrentHashMap.
 * La CLAVE del mapa es el campo {@code token}, que actúa como
 * identificador único de acceso para el usuario.
 */
public class DatosSimulacion {

    private String token;
    private DatosSolicitud solicitud;
    private String resultado;
    private String estado;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public DatosSimulacion() {
    }

    // Constructor con todos los parámetros
    public DatosSimulacion(String token, DatosSolicitud solicitud, String resultado, String estado, LocalDateTime fechaCreacion) {
        this.token = token;
        this.solicitud = solicitud;
        this.resultado = resultado;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // --- Getters y Setters explícitos ---

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DatosSolicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(DatosSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}