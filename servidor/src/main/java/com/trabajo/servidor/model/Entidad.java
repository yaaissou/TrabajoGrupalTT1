package com.trabajo.servidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad básica del sistema.
 * Representa cualquier recurso identificable por id y nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entidad {

    /** Identificador numérico único */
    private Long id;

    /** Nombre legible de la entidad */
    private String nombre;
}
