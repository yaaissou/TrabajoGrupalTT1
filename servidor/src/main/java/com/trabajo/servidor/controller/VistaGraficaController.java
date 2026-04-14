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
 *
 * Rutas manejadas:
 *  - GET /                          → página de inicio con formulario
 *  - GET /vista-grafica/{token}     → página "Misión Conseguida"
 *  - GET /error/acceso-denegado     → página de error por token inválido
 */
@Controller
public class VistaGraficaController {

    private final SimulacionService simulacionService;

    public VistaGraficaController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    /**
     * Página de inicio: formulario para solicitar un token.
     */
    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    /**
     * Muestra la página de éxito con los datos de la simulación.
     * Solo se llega aquí tras pasar por /api/simulacion/acceder/{token},
     * que ya valida el token antes de redirigir.
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
     */
    @GetMapping("/error/acceso-denegado")
    public String accesoDenegado() {
        return "accesoDenegado";
    }
}
