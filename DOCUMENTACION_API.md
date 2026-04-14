# Especificación de la API REST - Servidor de Simulación

Este documento define la interfaz de comunicación técnica (endpoints, métodos HTTP y formatos de carga útil) para interactuar con el Servidor de Simulación.

---

## 1. Solicitar Simulación
**Método HTTP:** `POST`  
**Endpoint:** `/api/simulacion/solicitar`  
**Descripción:** Procesa una nueva solicitud de simulación recibiendo un mapa de entidades y sus cantidades. Almacena la sesión en memoria y genera un identificador único para su seguimiento.

### Parámetros de la Petición (Request Body)
El servidor espera un objeto JSON estándar que represente el modelo `DatosSolicitud`, conteniendo un diccionario numérico (clave: ID Entidad, valor: Cantidad).

```json
{
  "nums": {
    "1": 5,
    "2": 10
  }
}
