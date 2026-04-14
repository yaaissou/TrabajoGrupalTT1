package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de configuración que el usuario envía al servidor
 * para solicitar una nueva simulación.
 *
 * El servidor los recibe en el body del POST /api/simulacion/solicitar.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosSolicitud {

    /** Nombre descriptivo que el usuario quiere dar a su simulación */
    private String nombreSimulacion;

    /** Descripción opcional del escenario a simular */
    private String descripcion;

    /** Número de iteraciones que debe ejecutar la simulación */
    private int iteraciones;
}
