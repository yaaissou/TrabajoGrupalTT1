package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.BattleshipRequest;
import com.trabajo.servidor.model.BattleshipResponse;
import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HundirFlotaController {

    private final HundirFlotaService hundirFlotaService;

    public HundirFlotaController(HundirFlotaService hundirFlotaService) {
        this.hundirFlotaService = hundirFlotaService;
    }

    /**
     * Recibe la solicitud del trabajo-individual, ejecuta la simulación
     * y devuelve el token entero que el cliente usará para recuperar los datos.
     *
     * Llamado por ServicioSolicitudes.solicitarSimulation()
     * POST http://localhost:8080/Solicitud/Solicitar?nombreUsuario=Marcos
     * Body: {"nums": {"1": n1, "2": n2, "3": n3}}
     */
    private static final int MAX_CELDAS = 50;

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

        String token = hundirFlotaService.simularPartida(nums);

        BattleshipResponse response = new BattleshipResponse();
        response.setDone(true);
        response.setTokenSolicitud(token);

        return ResponseEntity.ok(response);
    }

    /**
     * Devuelve el rawData de la simulación para que grid.html la visualice.
     *
     * Llamado por ServicioSolicitudes.descargarDatos()
     * POST http://localhost:8080/Resultados?nombreUsuario=Marcos&tok={token}
     * Body: null
     */
    @PostMapping("/Resultados")
    public ResponseEntity<BattleshipResponse> obtenerResultados(
            @RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
            @RequestParam("tok") String tok) {

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
