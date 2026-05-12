package com.trabajo.servidor.controller;

import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador MVC que gestiona las vistas de la interfaz gráfica (Thymeleaf)
 * para el juego de Hundir la Flota.
 * <p>
 * A diferencia del controlador REST, este controlador se encarga exclusivamente
 * de renderizar las páginas web HTML que el usuario verá en su navegador,
 * utilizando los datos generados por el motor de simulación.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
@Controller
@RequestMapping("/juego")
public class JuegoController {

    private final HundirFlotaService hundirFlotaService;

    /**
     * Inyección de dependencias para el servicio del juego.
     *
     * @param hundirFlotaService Servicio que contiene la lógica para recuperar los datos de las partidas.
     */
    public JuegoController(HundirFlotaService hundirFlotaService) {
        this.hundirFlotaService = hundirFlotaService;
    }

    /**
     * Muestra la página de confirmación con el token generado.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /juego/token?tok={numero}
     *
     * @param tok   El token numérico generado para la partida.
     * @param model Objeto {@link Model} de Spring para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf ({@code "juego/token"}).
     */
    @GetMapping("/token")
    public String mostrarToken(@RequestParam("tok") int tok, Model model) {
        model.addAttribute("token", tok);
        return "juego/token";
    }

    /**
     * Carga y muestra el tablero de la partida asociada a un token.
     * <p>
     * <strong>Método:</strong> GET<br>
     * <strong>URL:</strong> /juego/partida?tok={numero}
     * <p>
     * Flujo de validación:
     * <ul>
     * <li>Si el token no existe, redirige a una vista de error.</li>
     * <li>Si el token es válido, inyecta los datos de la cuadrícula en la vista principal.</li>
     * </ul>
     *
     * @param tok   El identificador único de la partida a visualizar.
     * @param model Objeto {@link Model} de Spring para pasar datos a la vista HTML.
     * @return El nombre de la plantilla de la partida ({@code "juego/partida"}) o la vista de error ({@code "juego/error"}).
     */
    @GetMapping("/partida")
    public String mostrarPartida(@RequestParam("tok") int tok, Model model) {
        String rawData = hundirFlotaService.obtenerRawData(tok);
        if (rawData == null) {
            model.addAttribute("error", "Token " + tok + " no encontrado.");
            return "juego/error";
        }
        model.addAttribute("tok", tok);
        model.addAttribute("datosGrid", rawData);
        return "juego/partida";
    }
}