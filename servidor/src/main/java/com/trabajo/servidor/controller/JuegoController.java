package com.trabajo.servidor.controller;

import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/juego")
public class JuegoController {

    private final HundirFlotaService hundirFlotaService;

    public JuegoController(HundirFlotaService hundirFlotaService) {
        this.hundirFlotaService = hundirFlotaService;
    }

    @GetMapping("/token")
    public String mostrarToken(@RequestParam("tok") String tok, Model model) {
        model.addAttribute("token", tok);
        return "juego/token";
    }

    @GetMapping("/partida")
    public String mostrarPartida(@RequestParam("tok") String tok, Model model) {
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
