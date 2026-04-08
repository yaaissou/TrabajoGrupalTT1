# Guía de Contribución y Organización del Equipo 

Este documento establece la estructura del equipo y las normativas de trabajo para el desarrollo del Servidor de Simulación. Todo el código y la documentación integrados en este repositorio deben seguir estas directrices.

## 1. Organización y Roles

El desarrollo del proyecto se estructura mediante la asignación de cuatro roles técnicos. Para fomentar un aprendizaje equitativo, se aplica un **sistema de rotación quincenal**. 

Cada dos semanas, los integrantes avanzan un puesto hacia la derecha siguiendo este orden de transición continuo:

`Programador` -> `Tester` -> `Redactor` -> `DevOps` -> `Programador`...

**Asignación Actual (Semanas 1 y 2):**
* **Programador:** Pablo Ramirez
* **Tester:** Marcos Zorzano
* **Redactor:** Yazid Aissou
* **DevOps:** Angel Muro

## 2. Responsabilidades Actuales

* **Programador:** Desarrolla el código fuente de los *endpoints* y la lógica de negocio.
* **Tester:** Diseña y ejecuta las pruebas. Ningún código se aprueba sin su validación.
* **Redactor:** Elabora y mantiene actualizada la especificación de la API (`DOCUMENTACION_API.md`).
* **DevOps:** Gestiona el repositorio, resuelve conflictos de fusión en Git y asegura que el proyecto compila correctamente.

## 3. Normativa de Trabajo (Git Flow)

Para garantizar la estabilidad del código, se prohíbe subir cambios directamente a la rama `main`. El flujo de trabajo estándar es el siguiente:

1. **Ramas de tarea:** Cada nueva funcionalidad o documento debe desarrollarse en una rama independiente (ej. `feature/catalogo` o `docs/api`).
2. **Commits:** Los mensajes de confirmación deben ser descriptivos e indicar el tipo de cambio (ej. `feat: añade controlador de solicitudes`, `docs: actualiza readme`).
3. **Pull Requests (PR):** Para unir el trabajo a la rama principal, se debe abrir una PR. Esta debe ser revisada por el *Tester* y finalmente fusionada por el *DevOps*.
