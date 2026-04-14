# Especificación de la API REST - Servidor de Simulación (Proyecto TT1)

Este documento detalla la interfaz técnica de comunicación del servidor. La API ha sido diseñada siguiendo los principios de la arquitectura REST para gestionar el ciclo de vida de las simulaciones mediante un sistema de estados y tokens de acceso.

## 1. Arquitectura y Flujo de Datos

El servidor actúa como un gestor de estados persistente en memoria. El flujo de interacción entre el cliente y el servidor se divide en tres fases críticas:

1.  **Registro**: El cliente envía la configuración técnica de la simulación.
2.  **Identificación**: El servidor genera un identificador único (Token UUID) que actúa como llave de acceso.
3.  **Visualización**: El usuario utiliza el token para acceder a la interfaz gráfica y recuperar los resultados calculados.



---

## 2. Definición de Endpoints

### 2.1. Solicitar Simulación
**Método:** `POST`  
**Ruta:** `/api/simulacion/solicitar`  
**Descripción:** Recibe los parámetros de configuración del usuario, instancia una simulación en el motor del servidor y devuelve el identificador único.

**Cuerpo de la Petición (JSON - `DatosSolicitud`):**
```json
{
  "nombreSimulacion": "Simulación Bosque v1",
  "descripcion": "Análisis de interacción entre especies",
  "iteraciones": 500
}
