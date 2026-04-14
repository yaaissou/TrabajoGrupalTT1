package com.trabajo.servidor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración provisional de Spring Security.
 *
 * El proyecto incluye spring-boot-starter-security, que por defecto
 * protege TODAS las rutas con autenticación HTTP Basic. Sin esta clase,
 * cualquier petición a /api/... recibiría un 401 Unauthorized.
 *
 * Esta configuración abre las rutas necesarias para la fase actual
 * de desarrollo. Cuando se implemente la autenticación real, se
 * restringirán los accesos aquí mismo.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad que Spring aplicará
     * a cada petición HTTP entrante.
     *
     * Reglas actuales:
     *  - CSRF deshabilitado: no es necesario en APIs REST stateless.
     *  - /api/simulacion/**  → acceso libre (los endpoints que estamos construyendo).
     *  - /error/**           → acceso libre (páginas de error y redirecciones).
     *  - /vista-grafica/**   → acceso libre (interfaz gráfica futura).
     *  - Cualquier otra ruta → requiere autenticación.
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
                // Permitir las rutas de error y la futura interfaz gráfica
                .requestMatchers("/error/**", "/vista-grafica/**").permitAll()
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
