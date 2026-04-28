package com.trabajo.servidor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración provisional de Spring Security.
 * <p>
 * El proyecto incluye spring-boot-starter-security, que por defecto
 * protege TODAS las rutas con autenticación HTTP Basic. Sin esta clase,
 * cualquier petición a /api/... recibiría un 401 Unauthorized.
 * <p>
 * Esta configuración abre las rutas necesarias para la fase actual
 * de desarrollo. Cuando se implemente la autenticación real, se
 * restringirán los accesos aquí mismo.
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad que Spring aplicará
     * a cada petición HTTP entrante.
     * <p>
     * Reglas actuales:
     * <ul>
     * <li>CSRF deshabilitado: no es necesario en APIs REST stateless.</li>
     * <li>/api/simulacion/** → acceso libre (los endpoints que estamos construyendo).</li>
     * <li>/error/** → acceso libre (páginas de error y redirecciones).</li>
     * <li>/vista-grafica/** → acceso libre (interfaz gráfica futura).</li>
     * <li>Cualquier otra ruta → requiere autenticación.</li>
     * </ul>
     *
     * @param http El objeto {@link HttpSecurity} proporcionado por Spring para configurar la seguridad web.
     * @return La cadena de filtros de seguridad ({@link SecurityFilterChain}) ya configurada y lista para aplicarse.
     * @throws Exception Si ocurre algún error interno durante la construcción de las reglas de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Deshabilitar CSRF para las llamadas REST desde clientes externos
                .csrf(csrf -> csrf.disable())

                // Reglas de autorización por ruta
                .authorizeHttpRequests(auth -> auth
                        // Permitir sin autenticación los endpoints de simulación
                        .requestMatchers("/api/simulacion/**").permitAll()
                        // Permitir la página de inicio, errores e interfaz gráfica
                        .requestMatchers("/", "/error/**", "/vista-grafica/**").permitAll()
                        // Endpoints consumidos por trabajo-individual
                        .requestMatchers("/Solicitud/**", "/Resultados").permitAll()
                        // Páginas del juego
                        .requestMatchers("/test.html", "/juego/**").permitAll()
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}