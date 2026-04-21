package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa los datos de configuración que el cliente envía al servidor
 * para solicitar la creación de una nueva simulación.
 * <p>
 * Estos datos son mapeados automáticamente desde un JSON recibido en el cuerpo
 * (body) de la petición HTTP en el endpoint:
 * <strong>POST /api/simulacion/solicitar</strong>.
 * <p>
 * <i>Nota: Esta clase utiliza anotaciones de Lombok para generar automáticamente
 * los métodos getter, setter y constructores en tiempo de compilación.</i>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosSolicitud {

    /** * Nombre descriptivo o título que el usuario asigna a la simulación
     * para facilitar su identificación posterior en el sistema.
     */
    private String nombreSimulacion;

    /** * Descripción detallada y opcional sobre el contexto, propósito o escenario
     * específico que se va a simular.
     */
    private String descripcion;

    /** * Número total de iteraciones o ciclos matemáticos que el motor de simulación
     * debe ejecutar para generar el resultado final.
     */
    private int iteraciones;
}