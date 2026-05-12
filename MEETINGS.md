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

---

## Acta 04: Diseño Lógico y Definición de Interfaz (UI)
* **Fecha:** 28 de Abril de 2026
* **Asistentes:** Pablo Ramirez, Marcos Zorzano, Yazid Aissou, Angel Muro.
* **Tipo de reunión:** Sesión de diseño conceptual y propuesta de experiencia de usuario.

#### 📝 Orden del Día
1. Definición teórica del motor de simulación y las variables matemáticas implicadas.
2. Brainstorming sobre la representación visual de los resultados en la interfaz del cliente.
3. Análisis de la coherencia entre los parámetros de entrada (`DatosSolicitud`) y la salida visual.
4. Consenso sobre el flujo de navegación antes de iniciar la fase de codificación.

#### 🤝 Acuerdos Adoptados
* **Abstracción del Motor Lógico:** Se acuerda finalizar el esquema matemático completo antes de realizar cualquier "picado" de código. El objetivo es asegurar que el algoritmo sea escalable y que el campo `resultado` del modelo pueda contener la estructura de datos necesaria para la representación final.
* **Consenso sobre la Interfaz Gráfica:** Se define que la visualización para el cliente debe basarse en una cuadrícula dinámica o una representación gráfica clara (grid) que permita interpretar los resultados de la simulación de un vistazo. Se prioriza la legibilidad de los datos procesados sobre la complejidad estética.
* **Mantenimiento de la Estabilidad:** Se decide no realizar modificaciones en la base de código esta semana para preservar el estado "verde" de los tests y la infraestructura lograda en la Semana 03, centrando el esfuerzo exclusivamente en el diseño y la documentación de requisitos.

#### 🚀 Próximos Pasos 
* **Programador (Angel):** Elaborar el pseudocódigo del algoritmo de simulación y definir la estructura interna que tendrá el resultado (ej. matriz, lista de eventos, etc.).
* **DevOps (Yazid):** Investigar la integración de librerías CSS o componentes para la renderización de la cuadrícula en las plantillas Thymeleaf.
* **Tester (Pablo):** Definir los casos de prueba teóricos y los rangos de valores esperados para validar la lógica matemática una vez se implemente.
* **Redactor (Marcos):** Documentar los bocetos de la interfaz de usuario y los requisitos del motor en el manual técnico, asegurando que la especificación de la API refleje cómo se visualizarán los datos finales.

---

## Acta 05: Implementación del Motor Lógico e Integración Continua
* **Fecha:** 5 de Mayo de 2026
* **Asistentes:** Pablo Ramirez, Marcos Zorzano, Yazid Aissou, Angel Muro.
* **Tipo de reunión:** Revisión de código (Code Review) y validación de infraestructura CI/CD.

#### 📝 Orden del Día
1. Revisión de la implementación en código del algoritmo matemático y motor de simulación.
2. Verificación de los flujos de trabajo (pipelines) en GitHub Actions tras la subida de los nuevos commits.
3. Planificación de la integración futura con la interfaz gráfica.

#### 🤝 Acuerdos Adoptados
* **Mantenimiento de Roles:** Se acuerda por unanimidad posponer la rotación de roles programada, manteniendo la configuración de la Semana 04. Esta decisión busca aprovechar la inercia técnica y el contexto actual de cada miembro para no interrumpir el desarrollo crítico del algoritmo.
* **Desarrollo Exclusivo del Motor Lógico:** Durante esta semana, el equipo se ha centrado estrictamente en programar el código del algoritmo matemático diseñado en la sesión anterior. Se ha sustituido la generación de resultados "mock" por la estructura de datos real que alimentará la simulación.
* **Integración Continua Exitosa (CI/CD):** Se ha comprobado y auditado el repositorio, confirmando que todos los *pushes* hacia la rama `early` han pasado los chequeos de GitHub Actions en verde. La compilación con Maven y los tests unitarios previos siguen funcionando correctamente con el nuevo código integrado.
* **Pausa Temporal en UI:** Para evitar falsos positivos en los errores de compilación, se acordó explícitamente no modificar las plantillas de Thymeleaf ni los controladores de vista en esta iteración, asegurando primero la estabilidad absoluta del "cerebro" del sistema.

#### 🚀 Próximos Pasos 
* **Programador (Angel):** Conectar el motor lógico recién desarrollado con el modelo de datos final, preparando la estructura que será enviada posteriormente a la vista del cliente.
* **Tester (Pablo):** Redactar y ejecutar las pruebas unitarias (JUnit) específicas para el nuevo algoritmo, introduciendo casos límite para asegurar que las matemáticas del motor no fallan bajo ninguna condición.
* **DevOps (Yazid):** Auditar que el contenedor Docker sigue compilando y levantando de forma óptima con la nueva carga computacional del proyecto.
* **Redactor (Marcos):** Documentar las nuevas clases y métodos creados para el algoritmo matemático, asegurando que la web del Javadoc se mantenga al día con la nueva lógica interna del servidor.

---

## Acta 06: Revisión de Infraestructura, Testing y Documentación
* **Fecha:** 12 de Mayo de 2026
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

