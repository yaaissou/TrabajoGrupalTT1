package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una entidad genérica y básica dentro del sistema.
 * <p>
 * Se utiliza para modelar cualquier recurso simple que necesite ser
 * identificado unívocamente por un identificador numérico y que posea
 * un nombre descriptivo o etiqueta.
 * <p>
 * <i>Nota: Esta clase utiliza anotaciones de Lombok para generar automáticamente
 * los métodos getter, setter y constructores en tiempo de compilación.</i>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entidad {

    /** * Identificador numérico único de la entidad.
     * <p>
     * Generalmente utilizado como clave primaria al almacenar o buscar
     * este recurso en una base de datos o colección.
     */
    private Long id;

    /** * Nombre legible para humanos que describe o titula a esta entidad.
     */
    private String nombre;
}