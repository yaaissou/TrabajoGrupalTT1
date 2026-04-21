# Registro de Reuniones y Actas 

Este documento contiene el registro cronológico de las reuniones de sincronización del equipo. Su propósito es auditar las decisiones arquitectónicas, metodológicas y el reparto de tareas acordado a lo largo del ciclo de vida del desarrollo del Servidor de Simulación.

---

## Acta 01: Reunión de Lanzamiento 
* **Fecha:** 7 de Abril de 2026
* **Asistentes:** Pablo Ramirez, Marcos Zorzano, Yazid Aissou, Angel Muro.
* **Tipo de reunión:** Planificación y Organización Inicial.

#### 📝 Orden del Día
1. Presentación formal del equipo de trabajo.
2. Definición de la metodología de trabajo para el desarrollo del Servidor de Simulación.
3. Reparto de responsabilidades para el primer ciclo de desarrollo (Semanas 1 y 2).

#### 🤝 Acuerdos Adoptados
* **Metodología de Rotación:** Se aprueba por unanimidad establecer un sistema de rotación de roles con una periodicidad quincenal para garantizar que todos los miembros adquieran experiencia en todas las capas del proyecto.
* **Asignación del Sprint 1 y 2 (Semanas 1 y 2):** * Queda designado **Pablo Ramirez** como responsable de desarrollo (*Programador*).
  * Queda designado **Marcos Zorzano** como responsable de calidad (*Tester*).
  * Queda designado **Yazid Aissou** como responsable de documentación técnica (*Redactor*).
  * Queda designado **Angel Muro** como administrador de infraestructura e integración (*DevOps*).

#### 🚀 Próximos Pasos 
* **DevOps:** Inicializar el repositorio base con Spring Boot y configurar los permisos en GitHub.
* **Redactor:** Redactar y publicar los documentos metodológicos iniciales (`CONTRIBUTING.md` y `MEETINGS.md`).
* **Equipo completo:** Iniciar el análisis funcional de los tres *endpoints* principales de la API REST para comenzar su implementación en los próximos días.

---

## Acta 02: Reunión de Estructuración
* **Fecha:** 14 de Abril de 2026
* **Asistentes:** Pablo Ramirez, Marcos Zorzano, Yazid Aissou, Angel Muro.
* **Tipo de reunión:** Elección de tipo de proyecto y estructuración técnica.

#### 📝 Orden del Día
1. Elecciones sobre la arquitectura de persistencia y flujo de envío de solicitudes.
2. Diseño de la estrategia de ramas en el repositorio y automatización de despliegue.
3. Planificación de la documentación técnica y empaquetado del servicio mediante contenedores.

#### 🤝 Acuerdos Adoptados
* **Jerarquía de Ramas:** Se acuerda la creación de un sistema de tres ramas: `main` (desarrollo activo), `early` (código validado por tests) y `stable` (versión pública definitiva).
* **Automatización CI/CD:** Implementación de *GitHub Actions* para ejecutar tests unitarios de forma automática en cada commit y gestionar la publicación de la documentación Javadoc vía *GitHub Pages*.
* **Estandarización del Entorno:** Adopción de Docker para el empaquetado del servidor, garantizando la compatibilidad del software en todos los puestos de trabajo.

#### 🚀 Próximos Pasos 
* **Programador (Pablo):** Desarrollar la lógica principal del servidor para la recepción de solicitudes, y la obtencion de tockens.
* **DevOps (Angel):** Configurar las ramas en GitHub y redactar el `Dockerfile` del proyecto.
* **Tester (Marcos):** Iniciar el diseño de la suite de pruebas unitarias para validar la integridad de la generación de tokens.
* **Redactor (Yazid):** Comenzar la redacción de la especificación técnica de la API en el archivo `DOCUMENTACION_API.md` basándose en el flujo de solicitudes acordado.

---

## Acta 03: Revisión de Infraestructura, Testing y Documentación
* **Fecha:** 21 de Abril de 2026
* **Asistentes:** Pablo Ramirez, Marcos Zorzano, Yazid Aissou, Angel Muro.
* **Tipo de reunión:** Resolución de incidencias técnicas, estabilización del código y generación de manuales.

#### 📝 Orden del Día
1. Formalización del cambio de roles semanales según lo establecido en `CONTRIBUTING.md`.
2. Evaluación y resolución de problemas de compilación en el entorno de pruebas y CI/CD (GitHub Actions).
3. Debate sobre la gestión de modelos de datos (uso de la librería Lombok frente a Vanilla Java).
4. Revisión de la documentación técnica generada (Javadoc) y empaquetado final del contenedor Docker.

#### 🤝 Acuerdos Adoptados
* **Downgrade a Java 17 (LTS):** Se aprueba por unanimidad bajar la versión del proyecto de Java 23 a Java 17 LTS en el archivo `pom.xml` y en los flujos de GitHub Actions. Esto asegura la compatibilidad total con herramientas de testing como Mockito y otorga mayor estabilidad a largo plazo.
* **Refactorización de Modelos y Lombok:** Se resolvió el conflicto de dependencias y errores de "clase ya definida" depurando las clases `DatosSolicitud` y `DatosSimulacion` para garantizar la robustez del pipeline en terminal, asegurando que constructores y getters/setters no generen fallos al compilar.
* **Estabilización de Tests:** Se corrigieron los problemas de inicialización (NullPointerException) en los tests de integración de la vista gráfica (`misionConseguida.html`), logrando que la suite completa de 11 tests pase con éxito en la integración continua.
* **Documentación Javadoc Integral:** Se acuerda mantener el estándar de calidad en la documentación, utilizando etiquetas HTML y anotaciones oficiales (`@param`, `@return`, `@author`) en toda la capa de Controladores, Modelos, Configuración y clase principal, solventando los problemas de generación de Maven.
* **Aislamiento en Docker:** Se valida la configuración del `Dockerfile` utilizando la imagen ligera `eclipse-temurin:17-jdk` y corrigiendo las rutas hacia `servidor/target/`, logrando un despliegue local exitoso por el puerto 8080.

#### 🚀 Próximos Pasos 
* **Programador (Angel):** Iniciar el desarrollo de la lógica matemática de simulación real, dado que la arquitectura base y la persistencia en memoria (`ConcurrentHashMap`) ya están validadas.
* **DevOps (Yazid):** Monitorizar el pipeline automático de GitHub Actions tras la sincronización de ramas (`main` a `early`) y asegurar que los despliegues en contenedores sigan siendo estables con los futuros cambios.
* **Tester (Pablo):** Diseñar nuevos casos de prueba para cubrir la lógica de negocio y los cálculos matemáticos que el programador implementará en el próximo ciclo, manteniendo el 100% de los tests en verde.
* **Redactor (Marcos):** Actualizar y expandir el documento `DOCUMENTACION_API.md` con las respuestas y peticiones finales, y volver a compilar el sitio web estático de Javadoc (`mvn javadoc:javadoc`) si se añaden nuevas clases.
