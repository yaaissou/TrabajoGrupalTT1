package com.trabajo.servidor.model;

import java.util.Map;

/**
 * Objeto de Transferencia de Datos (DTO) utilizado para la mensajería en RabbitMQ.
 * <p>
 * Encapsula la información necesaria que viaja desde el controlador REST
 * (Productor) hasta el servicio en segundo plano (Consumidor) para generar
 * un tablero válido.
 * * @author [Tu Nombre / Tu Grupo]
 * @version 1.0
 */
public class SimulacionMensaje {

    /** Identificador único (token) de la partida asociada a este mensaje. */
    private int token;

    /** * Mapeo de la cantidad de barcos solicitados.
     * La clave (Integer) representa el tipo o tamaño del barco, y el valor (Integer)
     * representa la cantidad deseada de ese tipo.
     */
    private Map<Integer, Integer> nums;

    /**
     * Constructor vacío requerido para la deserialización automática de JSON
     * a través del {@link com.trabajo.servidor.config.RabbitMQConfig#jsonMessageConverter()}.
     */
    public SimulacionMensaje() {}

    /**
     * Construye un nuevo mensaje listo para ser encolado.
     *
     * @param token El identificador numérico de la partida.
     * @param nums  El diccionario con la configuración de flota solicitada.
     */
    public SimulacionMensaje(int token, Map<Integer, Integer> nums) {
        this.token = token;
        this.nums = nums;
    }

    /** @return El token de la simulación. */
    public int getToken() { return token; }

    /** @param token El token a asignar a este mensaje. */
    public void setToken(int token) { this.token = token; }

    /** @return El mapa con las cantidades y tipos de barcos. */
    public Map<Integer, Integer> getNums() { return nums; }

    /** @param nums El diccionario de barcos a asignar. */
    public void setNums(Map<Integer, Integer> nums) { this.nums = nums; }
}