package com.trabajo.servidor.messaging;

import com.trabajo.servidor.config.RabbitMQConfig;
import com.trabajo.servidor.model.SimulacionMensaje;
import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Componente consumidor de RabbitMQ encargado de procesar las simulaciones
 * en segundo plano de forma asíncrona.
 * <p>
 * Actúa como un "escuchador" (Listener) que está permanentemente suscrito a la cola
 * de mensajería. Cuando el controlador encola una petición de partida, este componente
 * la extrae y lanza el cálculo matemático pesado, evitando que el hilo principal
 * (el que responde al cliente) se quede bloqueado.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
@Component
public class SimulacionListener {

    private final HundirFlotaService hundirFlotaService;

    /**
     * Inyección de dependencias por constructor.
     *
     * @param hundirFlotaService Servicio central que contiene la lógica de negocio y
     * los algoritmos matemáticos del juego Hundir la Flota.
     */
    public SimulacionListener(HundirFlotaService hundirFlotaService) {
        this.hundirFlotaService = hundirFlotaService;
    }

    /**
     * Método oyente que se dispara automáticamente cada vez que entra un nuevo mensaje
     * en la cola configurada ({@link RabbitMQConfig#QUEUE_NAME}).
     * <p>
     * <strong>Flujo de ejecución:</strong>
     * <ol>
     * <li>RabbitMQ detecta un mensaje y lo deserializa de JSON a {@link SimulacionMensaje}.</li>
     * <li>Spring invoca este método automáticamente pasando el objeto.</li>
     * <li>Se delega el procesamiento real de los barcos al servicio {@code hundirFlotaService}.</li>
     * </ol>
     *
     * @param mensaje Objeto que contiene el token de la partida y el mapa con la
     * cantidad de barcos (Alfa, Beta, Gamma) a colocar.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void procesarMensaje(SimulacionMensaje mensaje) {
        hundirFlotaService.procesarSolicitud(mensaje.getToken(), mensaje.getNums());
    }
}