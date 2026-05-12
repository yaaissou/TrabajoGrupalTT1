package com.trabajo.servidor.controller;

import com.trabajo.servidor.config.RabbitMQConfig;
import com.trabajo.servidor.model.BattleshipRequest;
import com.trabajo.servidor.model.BattleshipResponse;
import com.trabajo.servidor.model.SimulacionMensaje;
import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HundirFlotaController {

    private static final int MAX_CELDAS = 50;

    private final HundirFlotaService hundirFlotaService;
    private final RabbitTemplate rabbitTemplate;

    public HundirFlotaController(HundirFlotaService hundirFlotaService, RabbitTemplate rabbitTemplate) {
        this.hundirFlotaService = hundirFlotaService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * POST http://localhost:8080/Solicitud/Solicitar?nombreUsuario=Marcos
     * Body: {"nums": {"1": n1, "2": n2, "3": n3}}
     * Respuesta: {"done": true, "tokenSolicitud": 42}
     */
    @PostMapping("/Solicitud/Solicitar")
    public ResponseEntity<BattleshipResponse> solicitarSimulacion(
            @RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
            @RequestBody BattleshipRequest request) {

        Map<Integer, Integer> nums = request.getNums();
        int nAlfa  = nums != null ? nums.getOrDefault(1, 0) : 0;
        int nBeta  = nums != null ? nums.getOrDefault(2, 0) : 0;
        int nGamma = nums != null ? nums.getOrDefault(3, 0) : 0;

        if (nAlfa < 0 || nBeta < 0 || nGamma < 0) {
            BattleshipResponse err = new BattleshipResponse();
            err.setDone(false);
            err.setErrorMessage("Los valores no pueden ser negativos.");
            return ResponseEntity.badRequest().body(err);
        }

        int totalCeldas = nAlfa + nBeta * 2 + nGamma * 3;
        if (totalCeldas > MAX_CELDAS) {
            BattleshipResponse err = new BattleshipResponse();
            err.setDone(false);
            err.setErrorMessage("Demasiados barcos: " + totalCeldas
                    + " celdas solicitadas superan el límite de " + MAX_CELDAS + ".");
            return ResponseEntity.badRequest().body(err);
        }

        int token = hundirFlotaService.generarToken();
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, new SimulacionMensaje(token, nums));

        BattleshipResponse response = new BattleshipResponse();
        response.setDone(true);
        response.setTokenSolicitud(token);

        return ResponseEntity.ok(response);
    }

    /**
     * POST http://localhost:8080/Resultados?nombreUsuario=Marcos&tok=42
     * Respuesta: {"done": true, "tokenSolicitud": 42, "data": "10\nWIN\n..."}
     */
    @PostMapping("/Resultados")
    public ResponseEntity<BattleshipResponse> obtenerResultados(
            @RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
            @RequestParam("tok") int tok) {

        String rawData = hundirFlotaService.obtenerRawData(tok);

        BattleshipResponse response = new BattleshipResponse();
        if (rawData != null) {
            response.setDone(true);
            response.setTokenSolicitud(tok);
            response.setData(rawData);
        } else {
            response.setDone(false);
            response.setErrorMessage("Token no encontrado: " + tok);
        }

        return ResponseEntity.ok(response);
    }
}
