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
