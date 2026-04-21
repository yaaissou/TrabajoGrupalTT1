package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.DatosSimulacion;
import com.trabajo.servidor.service.SimulacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controlador que sirve las vistas Thymeleaf de la interfaz gráfica.
 * <p>
 * Rutas manejadas:
 * <ul>
 * <li><strong>GET /</strong> → página de inicio con formulario.</li>
 * <li><strong>GET /vista-grafica/{token}</strong> → página "Misión Conseguida".</li>
 * <li><strong>GET /error/acceso-denegado</strong> → página de error por token inválido.</li>
 * </ul>
 * @version 1.0
 */
@Controller
public class VistaGraficaController {

    private final SimulacionService simulacionService;

    /**
     * Inyecta el servicio de simulaciones necesario para recuperar los datos a mostrar.
     *
     * @param simulacionService Objeto que contiene la lógica de negocio para consultar las simulaciones guardadas.
     */
    public VistaGraficaController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    /**
     * Página de inicio: formulario para solicitar un token.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /
     *
     * @return El nombre de la plantilla Thymeleaf ({@code "inicio"}) que contiene el formulario principal.
     */
    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    /**
     * Muestra la página de éxito con los datos de la simulación.
     * <p>
     * Solo se llega aquí tras pasar por /api/simulacion/acceder/{token},
     * que ya valida el token antes de redirigir.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /vista-grafica/{token}
     *
     * @param token Identificador UUID de la simulación recibido como variable de ruta.
     * @param model Objeto {@link Model} de Spring utilizado para inyectar los datos en la vista HTML.
     * @return El nombre de la plantilla Thymeleaf ({@code "misionConseguida"}) si el token existe,
     * o una cadena de redirección hacia la página de error si no se encuentra.
     */
    @GetMapping("/vista-grafica/{token}")
    public String vistaGrafica(@PathVariable String token, Model model) {
        Optional<DatosSimulacion> simulacion = simulacionService.obtenerSimulacion(token);

        if (simulacion.isEmpty()) {
            return "redirect:/error/acceso-denegado";
        }

        model.addAttribute("simulacion", simulacion.get());
        model.addAttribute("token", token);
        return "misionConseguida";

    }

    /**
     * Muestra la página de error cuando el token no es válido.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /error/acceso-denegado
     *
     * @return El nombre de la plantilla Thymeleaf ({@code "accesoDenegado"}) para mostrar el mensaje de fallo.
     */
    @GetMapping("/error/acceso-denegado")
    public String accesoDenegado() {
        return "accesoDenegado";
    }
}