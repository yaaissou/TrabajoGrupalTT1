package com.trabajo.servidor.controller;

import com.trabajo.servidor.model.BattleshipRequest;
import com.trabajo.servidor.model.BattleshipResponse;
import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/Solicitud/Solicitar")
    public ResponseEntity<BattleshipResponse> solicitarSimulacion(
            @RequestParam(value = "nombreUsuario", required = false) String nombreUsuario,
            @RequestBody BattleshipRequest request) {

        int token = hundirFlotaService.simularPartida(request.getNums());

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
