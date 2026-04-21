package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa el resultado de una simulación generada por el servidor.
 * <p>
 * Esta clase es el <strong>VALOR</strong> que se almacena en el {@code ConcurrentHashMap} de la capa de servicio.
 * La <strong>CLAVE</strong> del mapa corresponde al campo {@code token}, que actúa como
 * identificador único y seguro de acceso para el usuario.
 * <p>
 * <i>Nota: Esta clase utiliza anotaciones de Lombok para generar automáticamente
 * los métodos getter, setter y constructores en tiempo de compilación.</i>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    // ========================================================================
    // CONSTRUCTORES
    // ========================================================================

    /**
     * Constructor por defecto (vacío).
     * <p>
     * Crea una nueva instancia sin inicializar sus atributos. Es especialmente
     * útil y requerido por frameworks como Spring (Jackson) para la correcta
     * deserialización de objetos JSON entrantes.
     */
    public DatosSimulacion() {
    }

    /**
     * Constructor con todos los parámetros.
     * <p>
     * Crea una nueva instancia inicializando todos los atributos de la simulación
     * en el momento de su instanciación.
     *
     * @param token         Identificador UUID único y seguro de la sesión.
     * @param solicitud     Objeto {@link DatosSolicitud} con la configuración enviada por el usuario.
     * @param resultado     Texto con los datos resultantes procesados por el servidor.
     * @param estado        Fase actual del ciclo de vida (ej. PENDIENTE, COMPLETADA).
     * @param fechaCreacion Marca temporal exacta en la que se generó este registro.
     */
    public DatosSimulacion(String token, DatosSolicitud solicitud, String resultado, String estado, LocalDateTime fechaCreacion) {
        this.token = token;
        this.solicitud = solicitud;
        this.resultado = resultado;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // ========================================================================
    // GETTERS Y SETTERS EXPLÍCITOS
    // ========================================================================

    /**
     * Obtiene el token único de esta simulación.
     *
     * @return El token UUID en formato texto.
     */
    public String getToken() {
        return token;
    }

    /**
     * Asigna un nuevo token a esta simulación.
     *
     * @param token El nuevo identificador UUID a asignar.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene los datos de configuración originales de la solicitud.
     *
     * @return Un objeto {@link DatosSolicitud} con los parámetros del usuario.
     */
    public DatosSolicitud getSolicitud() {
        return solicitud;
    }

    /**
     * Actualiza los datos de configuración de la solicitud.
     *
     * @param solicitud El nuevo objeto {@link DatosSolicitud} a asignar.
     */
    public void setSolicitud(DatosSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    /**
     * Obtiene el resultado procesado de la simulación.
     *
     * @return El resultado en formato texto (o mock).
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Establece o actualiza el resultado de la simulación.
     *
     * @param resultado El nuevo texto de resultado a guardar.
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    /**
     * Obtiene el estado actual del ciclo de vida de la simulación.
     *
     * @return El estado actual (ej. COMPLETADA).
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Modifica el estado actual de la simulación.
     *
     * @param estado El nuevo estado a registrar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la marca temporal de cuándo se creó la simulación.
     *
     * @return Objeto {@link LocalDateTime} con la fecha y hora exactas.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Actualiza la fecha de creación de la simulación.
     *
     * @param fechaCreacion La nueva fecha y hora a establecer.
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}