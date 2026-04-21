# 1. Usar una imagen oficial de Java 23
FROM eclipse-temurin:23-jdk

# 2. Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# 3. Copiar el ejecutable compilado desde tu ordenador al contenedor
COPY target/servidor-0.0.1-SNAPSHOT.jar app.jar

# 4. Comando exacto para arrancar el servidor
ENTRYPOINT ["java", "-jar", "/app.jar"]