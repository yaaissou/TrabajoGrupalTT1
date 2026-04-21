package com.trabajo.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que actúa como punto de entrada para la aplicación Spring Boot.
 * <p>
 * La anotación {@code @SpringBootApplication} engloba tres características clave:
 * <ul>
 * <li>Habilita la autoconfiguración del proyecto.</li>
 * <li>Activa el escaneo automático de componentes (para encontrar nuestros Controladores y Servicios).</li>
 * <li>Permite registrar *beans* adicionales en el contexto.</li>
 * </ul>
 * Al ejecutarse, esta clase levanta automáticamente el servidor web embebido (Tomcat)
 * en el puerto configurado (por defecto 8080) para empezar a recibir peticiones.
 * @version 1.0
 */
@SpringBootApplication
public class ServidorApplication {

	/**
	 * Método principal (entry point) estándar de Java.
	 * <p>
	 * Delega la ejecución a {@link SpringApplication#run(Class, String...)}
	 * para inicializar el contexto de Spring, arrancar el servidor web
	 * y dejar la aplicación lista para servir las rutas de la API REST y las vistas Thymeleaf.
	 *
	 * @param args Argumentos de línea de comandos que se pueden pasar al arrancar la aplicación.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServidorApplication.class, args);
	}
}