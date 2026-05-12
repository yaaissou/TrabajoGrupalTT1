package com.tt1.trabajo;

import com.tt1.trabajo.servicios.ServicioSolicitudes;
import com.tt1.trabajo.modelo.DatosSimulation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.tt1.trabajo.interfaces.InterfazContactoSim;
import com.tt1.trabajo.modelo.DatosSolicitud;

@Controller
public class SolicitudController {
	
	private final InterfazContactoSim ics;
	private final Logger logger;
	
	public SolicitudController(InterfazContactoSim ics, Logger logger) {
		this.ics = ics;
		this.logger = logger;
	}

    @GetMapping("/solicitud")
    public String solicitud(Model model) {
        model.addAttribute("entities", ics.getEntities());
        return "solicitud";
    }
    
    @PostMapping("/solicitud")
    public String handleSolicitud(@RequestParam Map<String, String> formData, Model model) {
    	Map<Integer, Integer> validData = new HashMap<>();
        List<String> errors = new ArrayList<>();

        formData.forEach((key, value) -> {
            try {
                int num = Integer.parseInt(value);
                if (num < 0) {
                    errors.add(key + " no puede ser negativo");
                }
                int id = Integer.parseInt(key);
                if (ics.isValidEntityId()) {
                	validData.put(id, num);
                } else {
                	errors.add(key + "no se corresponde con una entidad");
                }
            } catch (NumberFormatException e) {
                errors.add(key + " debe ser un número entero");
            }
        });
        if(!errors.isEmpty()) {
        	model.addAttribute("errors", errors);
        	logger.warn("Atendida petición con errores");
        } else {
        	logger.info("Atendida petición");
        	DatosSolicitud ds = new DatosSolicitud(validData);
        	int tok = ics.solicitarSimulation(ds);
        	if(tok != -1) {
        		model.addAttribute("token", tok);
        	} else {
        		logger.error("Error en comunicación con servidor de simulación");
        	}
        }
        return "formResult";
    }
    @GetMapping("/grid")
    public String mostrarGrid(@RequestParam("tok") int token, Model model) {


        DatosSimulation datos = ServicioSolicitudes.descargarDatos(token);

        if (datos != null && datos.getRawData() != null) {

            model.addAttribute("datosGrid", datos.getRawData());
        } else {
            model.addAttribute("error", "No se pudieron obtener los datos de la simulación.");
        }

        return "grid";
    }
}