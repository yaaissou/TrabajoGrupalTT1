package com.tt1.trabajo.interfaces;

import java.util.List;

import com.tt1.trabajo.modelo.DatosSimulation;
import com.tt1.trabajo.modelo.DatosSolicitud;
import com.tt1.trabajo.modelo.Entidad;

public interface InterfazContactoSim {
	public int solicitarSimulation(DatosSolicitud sol);
    public static DatosSimulation descargarDatos(int ticket) {
        return null;
    }
    public List<Entidad> getEntities();
	public boolean isValidEntityId();
}
