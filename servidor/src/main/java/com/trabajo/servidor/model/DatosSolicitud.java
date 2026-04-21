package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de configuración que el cliente envía al servidor
 * para solicitar la creación de una nueva simulación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
