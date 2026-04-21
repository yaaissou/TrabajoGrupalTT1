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
    // ========================================================================
    // CONSTRUCTORES
    // ========================================================================

    /**
     * Constructor por defecto (vacío).
     * <p>
     * Requerido obligatoriamente por herramientas como Spring y Jackson para poder
     * construir el objeto automáticamente cuando se recibe un JSON desde el cliente.
     */
    public DatosSolicitud() {
    }

    /**
     * Constructor con todos los parámetros.
     * <p>
     * Crea una nueva instancia de la solicitud inicializando todos sus valores
     * en el momento de la creación.
     *
     * @param nombreSimulacion Nombre o título descriptivo de la simulación.
     * @param descripcion      Explicación detallada del contexto o escenario a simular.
     * @param iteraciones      Número total de ciclos que deberá ejecutar el motor.
     */
    public DatosSolicitud(String nombreSimulacion, String descripcion, int iteraciones) {
        this.nombreSimulacion = nombreSimulacion;
        this.descripcion = descripcion;
        this.iteraciones = iteraciones;
    }

    // ========================================================================
    // GETTERS Y SETTERS EXPLÍCITOS
    // ========================================================================

    /**
     * Obtiene el nombre asignado a esta solicitud de simulación.
     *
     * @return El nombre descriptivo en formato texto.
     */
    public String getNombreSimulacion() {
        return nombreSimulacion;
    }

    /**
     * Asigna o actualiza el nombre de la simulación.
     *
     * @param nombreSimulacion El nuevo nombre a establecer.
     */
    public void setNombreSimulacion(String nombreSimulacion) {
        this.nombreSimulacion = nombreSimulacion;
    }

    /**
     * Obtiene la descripción detallada del escenario.
     *
     * @return El texto con la descripción, o nulo si no se proporcionó.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece una nueva descripción para el escenario de la simulación.
     *
     * @param descripcion El texto descriptivo a guardar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el número de iteraciones configuradas para el cálculo.
     *
     * @return La cantidad de ciclos matemáticos a ejecutar.
     */
    public int getIteraciones() {
        return iteraciones;
    }

    /**
     * Modifica el número de iteraciones que deberá realizar el motor.
     *
     * @param iteraciones La nueva cantidad entera de ciclos.
     */
    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }
}