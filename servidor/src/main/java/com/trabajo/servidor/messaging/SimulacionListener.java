package com.trabajo.servidor.messaging;

import com.trabajo.servidor.config.RabbitMQConfig;
import com.trabajo.servidor.model.SimulacionMensaje;
import com.trabajo.servidor.service.HundirFlotaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SimulacionListener {

    private final HundirFlotaService hundirFlotaService;

    public SimulacionListener(HundirFlotaService hundirFlotaService) {
        this.hundirFlotaService = hundirFlotaService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void procesarMensaje(SimulacionMensaje mensaje) {
        hundirFlotaService.procesarSolicitud(mensaje.getToken(), mensaje.getNums());
    }
}
