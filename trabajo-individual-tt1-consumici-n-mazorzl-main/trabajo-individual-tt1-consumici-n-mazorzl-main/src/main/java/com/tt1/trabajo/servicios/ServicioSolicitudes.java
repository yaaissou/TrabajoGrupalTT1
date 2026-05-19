package com.tt1.trabajo.servicios;
import com.tt1.trabajo.modelo.ResultsResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tt1.trabajo.interfaces.InterfazContactoSim;
import com.tt1.trabajo.modelo.DatosSolicitud;
import com.tt1.trabajo.modelo.DatosSimulation;
import com.tt1.trabajo.modelo.Entidad;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioSolicitudes implements InterfazContactoSim {
    private DatosSolicitud solicitudProvisional;

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        RestTemplate restTemplate = new RestTemplate();
        String urlApi = "http://localhost:8080/Solicitud/Solicitar?nombreUsuario=Marcos";

        try {
            ResultsResponse respuestaJson = restTemplate.postForObject(urlApi, sol, ResultsResponse.class);

            if (respuestaJson != null) {
                System.out.println("¡ÉXITO! La máquina ha creado la simulación con el TOKEN REAL: " + respuestaJson.getTokenSolicitud());
                return respuestaJson.getTokenSolicitud();
            }

        } catch (Exception e) {
            System.out.println("Error al crear la simulación real: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        return -1;
    }

    @Override
    public List<Entidad> getEntities() {
        List<Entidad> lista = new ArrayList<>();
        lista.add(new Entidad(1, "Entidad Alfa", "A"));
        lista.add(new Entidad(2, "Entidad Beta", "B"));
        lista.add(new Entidad(3, "Entidad Gamma", "G"));

        return lista;
    }

    public static DatosSimulation descargarDatos(int ticket) {
        RestTemplate restTemplate = new RestTemplate();
        String urlApi = "http://localhost:8080/Resultados?nombreUsuario=Marcos&tok=" + ticket;

        try {
            ResultsResponse respuestaJson = restTemplate.postForObject(urlApi, null, ResultsResponse.class);
            DatosSimulation datos = new DatosSimulation();

            if (respuestaJson != null) {
                System.out.println("========== RESPUESTA DE LA MÁQUINA ==========");
                System.out.println("1. ¿Ha terminado? (done): " + respuestaJson.isDone());
                System.out.println("2. Posible error: " + respuestaJson.getErrorMessage());
                System.out.println("3. Texto de colores (data): \n" + respuestaJson.getData());
                System.out.println("=============================================");

                if (respuestaJson.isDone()) {
                    datos.setRawData(respuestaJson.getData());
                } else {
                    datos.setRawData("");
                }
            }
            return datos;

        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isValidEntityId() {
        return true;
    }
}
