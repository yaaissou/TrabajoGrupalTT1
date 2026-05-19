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

/**
 * Controlador REST que expone la API del motor de simulación de Hundir la Flota.
 * <p>
 * Se encarga de recibir las peticiones JSON del cliente, validar las reglas
 * del juego (como el límite de celdas), delegar la carga de trabajo de forma
 * asíncrona a RabbitMQ y devolver los resultados procesados.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
@RestController
public class HundirFlotaController {

    /**
     * Límite máximo de celdas permitidas por tablero.
     * Equivale a la mitad de un tablero de 21 filas × 10 columnas (105 celdas).
     */
    private static final int MAX_CELDAS = 105;

    private final HundirFlotaService hundirFlotaService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param hundirFlotaService Servicio con la lógica de negocio de la flota.
     * @param rabbitTemplate     Plantilla para enviar mensajes asíncronos al bróker RabbitMQ.
     */
    public HundirFlotaController(HundirFlotaService hundirFlotaService, RabbitTemplate rabbitTemplate) {
        this.hundirFlotaService = hundirFlotaService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Solicita la creación de una nueva simulación de partida.
     * <p>
     * <strong>Endpoint:</strong> {@code POST /Solicitud/Solicitar}<br>
     * <p>
     * <strong>Validaciones:</strong>
     * <ul>
     * <li>Ningún tipo de barco puede tener una cantidad negativa.</li>
     * <li>La suma total de celdas ocupadas por los barcos no puede exceder {@value #MAX_CELDAS}.</li>
     * </ul>
     * Si las validaciones pasan, se genera un token y se encola un mensaje en RabbitMQ para su procesamiento asíncrono.
     *
     * @param nombreUsuario (Opcional) Nombre del jugador que solicita la partida.
     * @param request       Objeto {@link BattleshipRequest} con la cantidad de barcos deseada (Alfa, Beta, Gamma).
     * @return Una respuesta {@link BattleshipResponse} que contiene el éxito de la operación, mensajes de error o el token generado.
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
     * Consulta y recupera los resultados de una simulación previamente encolada.
     * <p>
     * <strong>Endpoint:</strong> {@code POST /Resultados}
     *
     * @param nombreUsuario (Opcional) Nombre del jugador que realiza la consulta.
     * @param tok           El token numérico identificador de la simulación.
     * @return Un objeto {@link BattleshipResponse} que incluye los datos finales ({@code rawData}) del tablero si el token existe.
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