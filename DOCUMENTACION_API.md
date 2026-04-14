# Especificación de la API REST 

Este documento detalla la interfaz técnica de comunicación del servidor. La API gestiona el ciclo de vida de las simulaciones mediante un sistema de estados y tokens de acceso, integrando endpoints REST puros y controladores de vistas (Thymeleaf).

---

## 1. Flujo de Operación

El sistema interactúa con el cliente mediante el siguiente ciclo:
1. **Solicitud:** El cliente envía los parámetros de configuración vía API.
2. **Identificación:** El servidor registra la simulación y devuelve un token único (UUID).
3. **Acceso y Validación:** El cliente utiliza el token en la URL para acceder al sistema. El servidor lo valida y redirige internamente.
4. **Visualización:** Se muestra la interfaz gráfica con los resultados procesados.

---

## 2. Endpoints de la API REST

### 2.1. Solicitar Nueva Simulación
* **Método HTTP:** `POST`
* **URL:** `/api/simulacion/solicitar`
* **Descripción:** Recibe la configuración técnica, registra la simulación en el almacenamiento en memoria y genera un token identificador.

**Estructura de la Petición (Request Body)**
El cuerpo del mensaje debe ser un objeto JSON que represente la clase `DatosSolicitud`:

### 2.2. Acceder mediante Token
* **Método:** `GET`
* **URL:** `/api/simulacion/acceder/{token}`
* **Descripción:** Valida la existencia del token en memoria y gestiona la redirección del usuario hacia la interfaz de visualización.

**Parámetros de Ruta**
* `token`: Identificador UUID obtenido en la fase de solicitud.

**Respuestas y Redirecciones**
* **`302 Found` (Éxito):** El token es válido. Se redirige automáticamente a `/vista-grafica/{token}` para mostrar los resultados.
* **`302 Found` (Error):** El token no existe o ha expirado. Se redirige a `/error/acceso-denegado`.

---

## 3. Endpoints de Visualización (Vistas)

### 3.1. Interfaz Gráfica de Resultados
* **Método:** `GET`
* **URL:** `/vista-grafica/{token}`
* **Descripción:** Recupera los datos completos de la simulación (estado, resultados y fecha de creación) y los renderiza en la plantilla de éxito.

### 3.2. Gestión de Errores
* **Método:** `GET`
* **URL:** `/error/acceso-denegado`
* **Descripción:** Muestra la interfaz de aviso cuando un usuario intenta acceder con credenciales inválidas.

---

## 4. Modelos de Datos Principales

### DatosSimulacion (Objeto de Respuesta)

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `token` | String | Identificador único de la sesión. |
| `estado` | String | Estado actual (PENDIENTE, COMPLETADA, etc.). |
| `resultado` | String | Datos procesados de la simulación. |
| `fechaCreacion` | DateTime | Marca temporal del registro. |
