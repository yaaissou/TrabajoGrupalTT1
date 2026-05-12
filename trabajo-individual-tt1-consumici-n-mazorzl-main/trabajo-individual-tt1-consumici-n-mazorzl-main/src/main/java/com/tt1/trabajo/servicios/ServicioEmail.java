package com.tt1.trabajo.servicios;
import org.springframework.stereotype.Service;

import com.tt1.trabajo.interfaces.InterfazEnviarEmails;
import com.tt1.trabajo.modelo.Destinatario;
import org.slf4j.Logger;

@Service
public class ServicioEmail implements InterfazEnviarEmails{
    private Logger logger;

    public ServicioEmail(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        logger.info("Enviando correo ficticio...");
        return true;
    }
}
