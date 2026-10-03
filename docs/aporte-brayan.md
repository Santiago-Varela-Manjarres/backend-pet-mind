# Aporte de Brayan · Avance 2

Capa de repositorios completa (14), las capas de servicio y controlador de `Usuario`, `Fundacion`, `Favorito`, `Notificacion` y `Archivo`, y el manejo centralizado de errores.

---

## 1. Capa de repositorios (texto para el README)

Cada entidad tiene su interfaz en `repository/` que extiende `JpaRepository<Entidad, Long>`. Spring Data genera la implementación a partir del nombre del método, así que no hay SQL escrito a mano.

Como el borrado es lógico (`BaseEntity.estadoActivo`), todas las consultas de listado terminan en `EstadoActivoTrue`: un registro "borrado" sigue en la tabla pero no aparece.

| Repositorio | Consulta personalizada | Para qué sirve |
|---|---|---|
| Los 14 | `findByEstadoActivoTrue()` | Listado general sin los registros borrados |
| `UsuarioRepository` | `existsByContactoEmailContacto(email)` | Regla 1: saber si el correo ya está registrado. Entra al embebido `contacto` |
| `MascotaRepository` | `findByEstadoPublicacionAndEstadoAdopcionAndEstadoActivoTrue(...)` | Listado de mascotas con filtros |
| `MascotaRepository` | `findByEspecieAndCiudadIgnoreCaseAndEstadoActivoTrue(...)` | Buscar por especie y ciudad sin importar mayúsculas |
| `MascotaRepository` | `findByFundacionIdAndEstadoActivoTrue(id)` | Mascotas de una fundación |
| `SolicitudAdopcionRepository` | `findByUsuarioIdAndEstadoActivoTrue(id)` | Panel del usuario |
| `SolicitudAdopcionRepository` | `findByMascotaFundacionIdAndEstadoAndEstadoActivoTrue(...)` | Tablero de la fundación. Recorre solicitud → mascota → fundación |
| `DonacionRepository` | `findByUsuarioIdAndEstadoActivoTrueOrderByFechaDonacionDesc(id)` | Mis donaciones, la más reciente primero |
| `ArchivoRepository` | `findByMascotaIdAndEstadoActivoTrueOrderByOrdenAsc(id)` | Galería de la mascota en orden |
| `NotificacionRepository` | `findByUsuarioIdAndLeidaFalseAndEstadoActivoTrue(id)` | Campana del navbar: notificaciones sin leer |
| `FavoritoRepository` | `findByUsuarioIdAndEstadoActivoTrue(id)` | Favoritos de un usuario |

**Pendiente:** `DonacionRepository.findByCampanaIdAndEstado...` quedó comentada. `Donacion` todavía no tiene la relación `campana`, y si se descomenta antes, la aplicación no arranca.

---

## 2. Endpoints

Todos devuelven `ResponseEntity`. Los errores se lanzan desde el servicio con las excepciones del paquete `exception` y el `GlobalExceptionHandler` los convierte en la respuesta (ver la sección 4), así que el controlador no tiene ningún `if` ni `try/catch`.

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/api/usuarios` | Lista los usuarios activos |
| GET | `/api/usuarios/{id}` | Un usuario (404 si no existe o está borrado) |
| POST | `/api/usuarios` | Crea un usuario (reglas 1 y 2) |
| PUT | `/api/usuarios/{id}` | Actualiza un usuario (reglas 1 y 2) |
| DELETE | `/api/usuarios/{id}` | Borrado lógico |
| GET | `/api/fundaciones` | Lista las fundaciones activas |
| GET | `/api/fundaciones/{id}` | Una fundación |
| POST | `/api/fundaciones` | Crea una fundación |
| PUT | `/api/fundaciones/{id}` | Actualiza una fundación |
| DELETE | `/api/fundaciones/{id}` | Borrado lógico |
| GET | `/api/favoritos` | Lista los favoritos activos |
| GET | `/api/favoritos/{id}` | Un favorito |
| GET | `/api/favoritos/por-usuario?usuarioId=1` | Favoritos de un usuario |
| POST | `/api/favoritos` | Crea un favorito (regla 3) |
| DELETE | `/api/favoritos/{id}` | Borrado lógico |
| GET | `/api/notificaciones` | Lista las notificaciones activas |
| GET | `/api/notificaciones/{id}` | Una notificación |
| GET | `/api/notificaciones/no-leidas?usuarioId=1` | Notificaciones sin leer de un usuario |
| POST | `/api/notificaciones` | Crea una notificación |
| PUT | `/api/notificaciones/{id}` | Actualiza una notificación |
| PATCH | `/api/notificaciones/{id}/leida` | Marca una notificación como leída |
| DELETE | `/api/notificaciones/{id}` | Borrado lógico |
| GET | `/api/archivos` | Lista los archivos activos |
| GET | `/api/archivos/{id}` | Un archivo |
| GET | `/api/archivos/por-mascota?mascotaId=1` | Galería de una mascota |
| POST | `/api/archivos` | Crea un archivo (regla 3) |
| PUT | `/api/archivos/{id}` | Actualiza los datos del archivo |
| DELETE | `/api/archivos/{id}` | Borrado lógico |

Las anotaciones obligatorias de la tarea se cubren así: `@PathVariable` en todas las rutas con `{id}`, `@RequestParam` en `por-usuario`, `no-leidas` y `por-mascota`, y `@RequestBody` en todos los `POST` y `PUT`.

---

## 3. Las 3 reglas de negocio

### Regla 1 · El correo del usuario es obligatorio y único

**Dónde:** `UsuarioServiceImpl.validarCorreoUnico()`. Se llama desde `crear()` y desde `actualizar()`; en `actualizar()` solo si el correo cambió.

**Qué condición evalúa:**
1. Si el JSON no trae `contacto` o `contacto.emailContacto`, falla.
2. Si `usuarioRepository.existsByContactoEmailContacto(correo)` devuelve `true`, otro usuario ya tiene ese correo.

**Por qué y cómo falla:** el correo es con lo que el usuario inicia sesión, así que no puede haber dos iguales. La base de datos también lo impediría (la columna es `unique`), pero con un mensaje genérico que no dice qué dato está repetido. Validarlo antes permite responder con un error claro:
- Sin correo → `ReglaNegocioException` → **400**
- Correo repetido → `RecursoDuplicadoException` → **409**

**Qué hace si pasa:** sigue con la regla 2 y guarda el usuario con `usuarioRepository.save()` → **201 Created**.

### Regla 2 · Un representante de fundación debe pertenecer a una fundación verificada

**Dónde:** `UsuarioServiceImpl.validarFundacionDelRepresentante()`, llamada desde `crear()` y `actualizar()`.

**Qué condición evalúa:**
1. Si el rol **no** es `REPRESENTANTE_FUNDACION`, le quita la fundación (solo los representantes la llevan) y termina.
2. Si es representante y no trae `fundacion.id`, falla.
3. Busca la fundación con `fundacionRepository.findById()`. Si no existe o está borrada, falla.
4. Si el `estadoVerificacion` de la fundación no es `VERIFICADA`, falla.

**Por qué y cómo falla:** una persona no puede administrar mascotas a nombre de una fundación que la plataforma no ha verificado.
- Sin fundación → `ReglaNegocioException` → **400**
- La fundación no existe → `RecursoNoEncontradoException` → **404**
- La fundación no está verificada → `ReglaNegocioException` → **400**

**Qué hace si pasa:** reemplaza la fundación que llegó en el JSON (que solo trae el `id`) por la fundación completa traída de la base, y el usuario se guarda con ella → **201**.

### Regla 3 · Un favorito o un archivo apunta a una sola cosa

**Dónde:** `FavoritoServiceImpl.validarUnSoloDestino()` y `ArchivoServiceImpl.validarUnSoloDueno()`, llamadas al inicio de `crear()`.

**Qué condición evalúa:** cuenta cuántas relaciones vienen llenas y exige que sea exactamente **1**.
- Favorito: `mascota`, `campana` o `fundacion`.
- Archivo: `mascota`, `solicitud`, `campana`, `historia` o `reporte`.

**Por qué y cómo falla:** en la base de datos esas columnas son nulas porque cada registro usa solo una. La base no puede impedir que lleguen dos o ninguna, así que el plan decidió validarlo en el servicio. Si el conteo es 0 o mayor que 1 → `ReglaNegocioException` → **400**.

**Qué hace si pasa:** busca en la base cada registro relacionado. Si alguno no existe lanza `RecursoNoEncontradoException` → **404**; si todos existen, los asigna y guarda con `save()` → **201**.

---

## 4. Manejo de errores

Los servicios no arman respuestas HTTP: lanzan una excepción del paquete `exception` y el `GlobalExceptionHandler` (`@RestControllerAdvice`) la convierte en la respuesta con el código que corresponde. Aplica a todos los servicios, no solo a los de esta entrega.

| Excepción | Cuándo | Código |
|---|---|---|
| `RecursoNoEncontradoException` | El registro no existe o está borrado lógicamente | **404** |
| `ReglaNegocioException` | Falta un dato obligatorio o se incumple una regla de negocio | **400** |
| `RecursoDuplicadoException` | Un dato que debe ser único ya está registrado, como el correo del usuario | **409** |
| `DataIntegrityViolationException` | La base de datos rechaza el registro: un valor `unique` repetido (como el NIT de una fundación) o un campo obligatorio vacío | **409** / **400** |
| Errores de Spring MVC | JSON mal formado, parámetro faltante o de otro tipo, ruta inexistente, método no permitido | **400** / **404** / **405** / **415** |
| Cualquier otra | Un error no previsto. El detalle queda en el log, nunca en la respuesta | **500** |

Todas las respuestas de error tienen la misma forma (`ErrorResponse`), con los campos del error por defecto de Spring Boot:

```json
{
  "timestamp": "2026-10-03T12:41:53.799",
  "status": 404,
  "error": "Not Found",
  "message": "No existe un usuario con id 99",
  "path": "/api/usuarios/99"
}
```

---

## 5. Probar en Postman

1. Importar `docs/postman/PetMind-Brayan.postman_collection.json`.
2. Levantar la aplicación (`./mvnw spring-boot:run`).
3. Correr la colección completa, en orden. La carpeta `0. Preparacion` crea las fundaciones y guarda sus `id` para las demás peticiones.

Cada regla tiene su carpeta con un caso **OK** y sus casos **FALLA**, y cada petición comprueba el código de estado esperado. La colección se puede repetir: usa un sufijo con la hora para no chocar con correos, NIT y nombres ya registrados.

El caso "OK - Archivo de una mascota" necesita que exista una mascota; por defecto usa la variable `mascotaId = 1`. Hasta que haya endpoint de mascotas, ese caso responde 404.
