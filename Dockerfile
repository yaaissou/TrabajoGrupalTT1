FROM eclipse-temurin:17-jdk
EXPOSE 8080
# Fíjate en la ruta que empieza por servidor/
COPY servidor/target/servidor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]