# Plan de trabajo · Segundo momento

**Entregable:** capas restantes del backend (repository, service, controller) y reescritura del README.
**Duración:** 5 días · **Equipo:** 3 personas · **Alcance:** 14 entidades del modelo v3 · **Sin DTOs.**

---

## Contexto

El primer momento dejó el modelo de datos y 5 entidades JPA. Al comparar el diagrama original con las visuales de la aplicación (42 pantallas en `02-Visuales PetMind`) aparecieron dos desfases: el modelo incluía un módulo de cursos que no existe en ninguna pantalla, y le faltaban las entidades de fundaciones, historias, reportes, campañas y archivos que las pantallas sí exigen.

De ahí salió el **modelo v3** (`Diagrama-DB-PetMind-v3.drawio`): 14 tablas, 27 relaciones. Es el mínimo que sirve las 42 pantallas. Este plan construye las capas restantes sobre ese modelo.

---

## Dos ajustes que el plan resuelve

### 1. El reparto por capas bloquea en cadena

Santiago no puede empezar sin los repositorios de Brayan, y Emmanuel no puede empezar sin los servicios de Santiago. Con una semana, esperar turnos no alcanza.

La solución no es cambiar el reparto sino **separar firmas de implementación**: el día 2 en la mañana cada uno publica solo las interfaces de su capa, se mergean juntas, y desde ahí los tres implementan en paralelo contra contratos ya acordados.

### 2. Exponer entidades directas exige relaciones unidireccionales

Sin DTOs, una `Mascota` con lista de `SolicitudAdopcion` que a su vez apunta a `Mascota` serializa en bucle infinito.

La regla que lo evita sin escribir una sola clase extra: **solo se mapea el lado `@ManyToOne`, nunca `@OneToMany`**. Las colecciones se piden al repositorio (`solicitudRepository.findByMascotaId(id)`), que además es más eficiente.

---

## Día 1 · Modelo v3 completo

Es el día crítico. Si al final del día no compila, toda la semana se corre.

### Bloque A · Preparación — Brayan, 30 min, antes que todo

Agregar al `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Reset del esquema en Neon.** `ddl-auto=update` nunca renombra ni borra: no va a convertir `refugios` en `fundaciones`, no va a eliminar `cursos`, `leccion`, `inscripcion_curso` ni `certificados`, y no va a volver `usuario_id` nullable en `donaciones`. Hay que correr una vez en la consola de Neon:

```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```

No hay datos reales, así que el costo es cero. **Avisar al grupo antes de ejecutarlo.**

Crear la rama base `feat/modelo-v3` a partir de `develop`, de la que salen las tres ramas individuales.

### Bloque B · Enums y embeddables — Emmanuel, ~2 h

**Ajustar 3 enums existentes:**

| Enum | Cambio |
|---|---|
| `RolUsuario` | `REFUGIO` → `REPRESENTANTE_FUNDACION` |
| `EstadoSolicitud` | De 3 a 8 valores: `BORRADOR`, `ENVIADA`, `EN_REVISION`, `ENTREVISTA`, `VISITA_HOGAR`, `APROBADA`, `RECHAZADA`, `CANCELADA` |
| `EstadoDonacion` | Agregar `FALLIDA` y `REEMBOLSADA` |

**Crear 17 enums nuevos:**

`TipoToken` · `EstadoVerificacion` · `EstadoPublicacion` · `TipoMedio` · `CategoriaArchivo` · `TipoVivienda` · `Modalidad` · `EstadoCita` · `CategoriaCampana` · `EstadoCampana` · `Frecuencia` · `EstadoRecurrencia` · `TipoHistoria` · `TipoCaso` · `NivelUrgencia` · `EstadoReporte` · `EstadoAtencion` · `TipoNotificacion`

**Embeddables.** Se reusan `Direccion`, `InformacionContacto` y `CualidadesFisicas` tal cual están. Se agregan tres para domar las tablas anchas:

- `Afinidad` — los 4 enteros de las barras del perfil de la mascota
- `DatosHogar` — los campos del formulario dentro de `SolicitudAdopcion`
- `Cita` — los 4 campos `cita_*` de la entrevista

### Bloque C · Las 14 entidades — los tres, ~4 h

| Quién | Entidades |
|---|---|
| **Brayan** | `Usuario`, `Fundacion`, `Favorito`, `Notificacion`, `Archivo` |
| **Santiago** | `Mascota`, `SolicitudAdopcion`, `Historia`, `Actualizacion`, `ReporteFundacion` |
| **Emmanuel** | `CampanaDonacion`, `GastoCampana`, `Donacion`, `ReporteAnimal` |

**Reglas no negociables del día 1:**

- Toda relación es `@ManyToOne(fetch = FetchType.LAZY)`. Cero `@OneToMany`, cero `@ManyToMany`.
- `@JsonIgnore` en `contrasena` y `tokenCodigo` de `Usuario`.
- `Archivo` y `Favorito` llevan sus FK nulas; la validación de "solo una llena" va en el servicio, no en la entidad.
- `Donacion.donacionOrigen` es un `@ManyToOne` hacia sí misma.
- Todas siguen heredando de `BaseEntity`.

**Criterio de salida:** `./mvnw spring-boot:run` levanta y Hibernate crea 14 tablas en Neon. **Merge a `develop` el mismo día**, aunque sea tarde.

### Bloque D · Contratos — los tres, 30 min al cierre

Antes de irse, acordar y escribir en un `CONTRATOS.md` temporal: el nombre de cada repositorio, servicio y controlador, la ruta base de cada recurso y los métodos que expone cada capa. Se borra al final de la semana. Es lo que desbloquea el día 2.

---

## Día 2 · Firmas en la mañana, implementación en la tarde

### Mañana — bloqueante, ~2 h, en paralelo

| Quién | Entrega |
|---|---|
| **Brayan** | Las 14 interfaces `JpaRepository` **completas**, con sus query methods. Spring Data las implementa solo, así que esto ya queda terminado. |
| **Santiago** | Las 14 interfaces de servicio, **solo firmas**. Sin implementación. |
| **Emmanuel** | `GlobalExceptionHandler`, `ErrorResponse`, `RecursoNoEncontradoException`, y los 14 `@RestController` con sus rutas declaradas devolviendo `null`. |

**Merge conjunto a `develop` al mediodía.** A partir de acá nadie espera a nadie.

### Consultas que necesita cada repositorio

Derivadas de lo que piden las pantallas:

```java
// MascotaRepository — listado con filtros y perfil
List<Mascota> findByEstadoPublicacionAndEstadoAdopcion(EstadoPublicacion p, EstadoAdopcion a);
List<Mascota> findByEspecieAndCiudadIgnoreCase(Especie especie, String ciudad);
List<Mascota> findByFundacionId(Long fundacionId);

// SolicitudAdopcionRepository — panel del usuario y tablero de la fundación
List<SolicitudAdopcion> findByUsuarioId(Long usuarioId);
List<SolicitudAdopcion> findByMascotaFundacionIdAndEstado(Long fundacionId, EstadoSolicitud estado);

// DonacionRepository — mis donaciones y progreso de campaña
List<Donacion> findByUsuarioIdOrderByFechaDonacionDesc(Long usuarioId);
List<Donacion> findByCampanaIdAndEstado(Long campanaId, EstadoDonacion estado);

// ArchivoRepository — galería de la mascota
List<Archivo> findByMascotaIdOrderByOrdenAsc(Long mascotaId);

// NotificacionRepository — campana del navbar
List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId);
```

> **Ojo con `BaseEntity.estadoActivo`.** El borrado es lógico, así que las consultas de listado llevan `AndEstadoActivoTrue`.

### Tarde — implementación en paralelo

Cada uno arranca por el nivel A.

---

## Días 3 y 4 · Implementación por niveles

**Nivel A — CRUD completo + consultas de filtro.** Prioridad absoluta:

`Usuario` · `Fundacion` · `Mascota` · `SolicitudAdopcion` · `CampanaDonacion` · `Donacion`

**Nivel B — CRUD simple:**

`Historia` · `ReporteAnimal` · `Archivo` · `GastoCampana` · `Actualizacion` · `Favorito` · `Notificacion` · `ReporteFundacion`

**Regla de corte:** si el viernes no alcanza, el nivel B se entrega con `GET` y `POST` únicamente. El nivel A va completo sí o sí.

**Día 4 en la tarde — integración.** Un `CommandLineRunner` con datos semilla (2 fundaciones, 6 mascotas, 2 campañas, 3 solicitudes) y una colección de Postman o un archivo `.http` probando los endpoints del nivel A de punta a punta.

---

## Día 5 · README y entrega

Lo redacta **Santiago**, que ya tiene esa responsabilidad asignada, con un aporte escrito de cada uno sobre su capa.

Estructura propuesta:

1. Qué es PetMind y qué resuelve
2. Equipo y responsabilidades — actualizar: ahora son capas, no tareas sueltas
3. Tecnologías — agregar Bean Validation
4. Arquitectura en capas — diagrama de texto `Controller → Service → Repository → Entity`
5. **Modelo de datos v3** — el diagrama nuevo, las 14 tablas, los enums, y una nota de por qué se eliminó el módulo de cursos
6. **Decisiones de diseño y sus límites** — relaciones unidireccionales, sin DTOs, `open-in-view` activo, borrado lógico. Que quede escrito que son decisiones conscientes con un costo conocido, no descuidos.
7. Endpoints de la API — tabla con método, ruta, qué hace y quién la construyó
8. Instalación, variables de entorno y ejecución
9. Flujo de trabajo en Git

Exportar además el `.drawio` v3 a PNG y reemplazar `docs/diagrama-db-petmind.png`.

---

## Riesgos

| Riesgo | Probabilidad | Mitigación |
|---|---|---|
| El día 1 no cierra y se corre toda la semana | **Alta** | Entidades repartidas, no secuenciales. Si a las 6 p.m. falta algo, se mergea lo que compile y lo pendiente pasa a la mañana del día 2, antes que las firmas. |
| Tres personas contra una sola base Neon con `ddl-auto=update` | **Alta** | Usar **branches de Neon**: una rama de base de datos por persona, cada una con su `DB_URL` en el `.env` local. Elimina el problema de raíz. |
| Conflictos de merge en el día 2 | Media | Cada uno toca archivos distintos por diseño. El `CONTRATOS.md` evita que dos inventen nombres diferentes para lo mismo. |
| Ciclos de serialización al probar endpoints | Media | La regla de unidireccionalidad los previene. Si aparece uno, es que alguien agregó un `@OneToMany`. |
| `LazyInitializationException` al serializar | Media | Dejar `spring.jpa.open-in-view=true` (el valor por defecto) esta semana y documentarlo como límite conocido en el README. |

---

## Definición de terminado

- [ ] Las 14 entidades levantan el esquema en Neon sin errores
- [ ] 14 repositorios con las consultas que el front necesita
- [ ] 14 servicios con validación de reglas de negocio y borrado lógico
- [ ] 14 controladores con manejo de errores centralizado
- [ ] Los 6 recursos del nivel A probados de punta a punta
- [ ] README reescrito con el modelo v3, los endpoints y las decisiones de diseño
- [ ] `docs/diagrama-db-petmind.png` actualizado al v3
- [ ] `develop` compila y arranca desde cero

---

## Decisiones pendientes

Dos cosas que conviene resolver antes de arrancar el día 1:

1. **¿Usan branches de Neon?** Es la mitigación del peor riesgo de la semana.
2. **¿El nivel B puede entregarse parcial?** Define la regla de corte del viernes.
