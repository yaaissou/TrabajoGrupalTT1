package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Resultado de una simulación generada por el servidor.
 *
 * Esta clase es el VALOR que se almacena en el ConcurrentHashMap.
 * La CLAVE del mapa es el campo {@code token}, que actúa como
 * identificador único de acceso para el usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosSimulacion {

    /**
     * Token UUID único que identifica esta simulación.
     * El usuario lo usa como llave de acceso a la interfaz gráfica.
     */
    private String token;

    /** Datos originales que el usuario envió en la solicitud */
    private DatosSolicitud solicitud;

    /** Resultado generado por el servidor (mock en esta fase) */
    private String resultado;

    /**
     * Estado del ciclo de vida de la simulación.
     * Valores posibles: PENDIENTE, EN_PROCESO, COMPLETADA, ERROR
     */
    private String estado;

    /** Fecha y hora exactas en que se creó la simulación */
    private LocalDateTime fechaCreacion;
}
