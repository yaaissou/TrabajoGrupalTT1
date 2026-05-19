package com.trabajo.servidor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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