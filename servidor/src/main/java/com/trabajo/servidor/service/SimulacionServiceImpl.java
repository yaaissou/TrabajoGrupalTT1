package com.trabajo.servidor.service;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.model.DatosSolicitud;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación del servicio de simulaciones.
 *
 * Usa un ConcurrentHashMap como base de datos en memoria:
 *   - CLAVE  → token (String UUID)
 *   - VALOR  → objeto DatosSimulacion completo
 *
 * ConcurrentHashMap garantiza operaciones atómicas y seguridad
 * en entornos con múltiples hilos concurrentes.
 */
@Service
public class SimulacionServiceImpl implements SimulacionService {

    /**
     * "Base de datos" en memoria.
     * Clave:  token único (UUID en formato String)
     * Valor:  datos completos de la simulación
     */
    private final Map<String, DatosSimulacion> almacenamiento = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     *
     * Pasos que ejecuta este método:
     *  1. Genera un UUID aleatorio como token único.
     *  2. Construye un objeto DatosSimulacion con datos mock.
     *  3. Lo guarda en el mapa con el token como clave.
     *  4. Devuelve el token al controlador para entregárselo al usuario.
     */
    @Override
    public String solicitarSimulacion(DatosSolicitud solicitud) {

        // Paso 1: generar el token único para esta simulación
        String token = UUID.randomUUID().toString();

        // Paso 2: construir el resultado mock (en fases posteriores
        //         aquí irá la lógica real de la simulación)
        String resultadoMock = String.format(
                "Simulación '%s' ejecutada con %d iteraciones. [MOCK]",
                solicitud.getNombreSimulacion(),
                solicitud.getIteraciones()
        );

        // Paso 3: crear el objeto que se almacenará en memoria
        DatosSimulacion simulacion = new DatosSimulacion(
                token,
                solicitud,
                resultadoMock,
                "COMPLETADA",
                LocalDateTime.now()
        );

        // Paso 4: persistir en el mapa usando el token como clave
        almacenamiento.put(token, simulacion);

        // Paso 5: devolver solo el token al controlador
        return token;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<DatosSimulacion> obtenerSimulacion(String token) {
        // Optional.ofNullable devuelve vacío si el token no existe
        return Optional.ofNullable(almacenamiento.get(token));
    }

    /** {@inheritDoc} */
    @Override
    public boolean tokenEsValido(String token) {
        return almacenamiento.containsKey(token);
    }

    /** {@inheritDoc} */
    @Override
    public List<DatosSimulacion> obtenerTodas() {
        return new ArrayList<>(almacenamiento.values());
    }
}
