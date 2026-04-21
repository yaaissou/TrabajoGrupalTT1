package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Resultado de una simulación generada por el servidor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosSimulacion {

    private String token;
    private DatosSolicitud solicitud;
    private String resultado;
    private String estado;
    private LocalDateTime fechaCreacion;

}