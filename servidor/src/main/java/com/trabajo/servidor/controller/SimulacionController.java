package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.DatosSolicitud;
import com.trabajo.servidor.service.SimulacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador REST que gestiona el ciclo de vida de las simulaciones
 * basado en tokens.
 * <p>
 * Flujo completo:
 * <ol>
 * <li>El usuario hace POST a /api/simulacion/solicitar con sus datos.</li>
 * <li>El servidor guarda la simulación y devuelve un token único.</li>
 * <li>El usuario hace GET a /api/simulacion/acceder/{token}.</li>
 * <li>El servidor valida el token y redirige a la interfaz gráfica.</li>
 * </ol>
 * <p>
 * Nota: se usa {@code @Controller} (no {@code @RestController}) para que el método
 * de acceso pueda retornar una cadena "redirect:..." que Spring MVC
 * interpreta como una redirección HTTP 302.
 * El método POST lleva {@code @ResponseBody} para devolver texto plano.
 * @version 1.0
 */
@Controller
@RequestMapping("/api/simulacion")
public class SimulacionController {

    private final SimulacionService simulacionService;

    /**
     * Inyección de dependencia por constructor (práctica recomendada).
     * Spring inyecta automáticamente el bean SimulacionServiceImpl.
     *
     * @param simulacionService Objeto que contiene la lógica de negocio para gestionar simulaciones.
     */
    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    // ---------------------------------------------------------------
    // ENDPOINT 1: Solicitar una nueva simulación
    // ---------------------------------------------------------------

    /**
     * Recibe los datos de configuración del usuario, genera una simulación
     * mock, la guarda en memoria y devuelve el token de acceso.
     * <p>
     * <strong>Método:</strong> POST<br>
     * <strong>URL:</strong> /api/simulacion/solicitar
     * <p>
     * <strong>Ejemplo de body:</strong>
     * <pre>{@code
     * {
     * "nombreSimulacion": "Prueba 1",
     * "descripcion": "Escenario de carga inicial",
     * "iteraciones": 100
     * }
     * }</pre>
     *
     * @param solicitud JSON mapeado a la clase {@link DatosSolicitud} con los parámetros de la petición.
     * @return {@link ResponseEntity} que contiene el token UUID en texto plano (HTTP 200).
     */
    @PostMapping("/solicitar")
    @ResponseBody
    public ResponseEntity<String> solicitarSimulacion(@RequestBody DatosSolicitud solicitud) {

        // Delegar al servicio: él genera el token, construye la simulación
        // y la guarda en el ConcurrentHashMap
        String token = simulacionService.solicitarSimulacion(solicitud);

        // Devolver solo el token al cliente como texto plano
        return ResponseEntity.ok(token);
    }

    // ---------------------------------------------------------------
    // ENDPOINT 2: Acceder a la interfaz gráfica con el token
    // ---------------------------------------------------------------

    /**
     * Valida el token recibido en la URL y redirige al destino correcto.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /api/simulacion/acceder/{token}
     * <ul>
     * <li>Si el token <strong>ES válido</strong>   → redirige a /vista-grafica/{token}</li>
     * <li>Si el token <strong>NO es válido</strong> → redirige a /error/acceso-denegado</li>
     * </ul>
     * <p>
     * La cadena "redirect:..." hace que Spring MVC envíe una respuesta
     * HTTP 302 con el header Location apuntando a la nueva ruta.
     *
     * @param token El UUID recibido como variable de ruta desde el cliente.
     * @return Cadena de redirección interpretada por Spring MVC.
     */
    @GetMapping("/acceder/{token}")
    public String accederConToken(@PathVariable String token) {

        // Comprobar si el token existe en el almacenamiento en memoria
        if (simulacionService.tokenEsValido(token)) {

            // Token válido → redirigir a la futura interfaz gráfica
            // Spring MVC enviará HTTP 302 → Location: /vista-grafica/{token}
            return "redirect:/vista-grafica/" + token;

        } else {

            // Token no encontrado → redirigir a la página de error
            // Spring MVC enviará HTTP 302 → Location: /error/acceso-denegado
            return "redirect:/error/acceso-denegado";
        }
    }
}