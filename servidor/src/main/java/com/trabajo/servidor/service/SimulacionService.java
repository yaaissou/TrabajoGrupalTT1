package com.trabajo.servidor.service;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.model.DatosSolicitud;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de la lógica de negocio para la gestión de simulaciones.
 *
 * Define las operaciones que el controlador puede invocar,
 * sin acoplarse a ninguna implementación concreta.
 */
public interface SimulacionService {

    /**
     * Procesa una solicitud, genera la simulación y la persiste en memoria.
     *
     * @param solicitud datos de configuración enviados por el usuario
     * @return el token UUID generado, que el usuario usará para acceder
     */
    String solicitarSimulacion(DatosSolicitud solicitud);

    /**
     * Busca una simulación por su token.
     *
     * @param token identificador único de la simulación
     * @return Optional con la simulación si existe, vacío si no
     */
    Optional<DatosSimulacion> obtenerSimulacion(String token);

    /**
     * Comprueba si un token existe en el almacenamiento.
     *
     * @param token identificador a validar
     * @return true si el token es válido, false en caso contrario
     */
    boolean tokenEsValido(String token);

    /**
     * Devuelve todas las simulaciones almacenadas en memoria.
     *
     * @return lista con todos los objetos DatosSimulacion
     */
    List<DatosSimulacion> obtenerTodas();
}
