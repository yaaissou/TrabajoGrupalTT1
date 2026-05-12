package com.tt1.trabajo.interfaces;

import com.tt1.trabajo.modelo.Destinatario;

public interface InterfazEnviarEmails {
	public boolean enviarEmail(Destinatario dest, String email);
}
