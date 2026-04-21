package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de configuración que el usuario envía al servidor
 * para solicitar una nueva simulación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosSolicitud {

    private String nombreSimulacion;
    private String descripcion;
    private int iteraciones;

}