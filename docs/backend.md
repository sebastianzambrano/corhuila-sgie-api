# Backend SGIE – Documentación Técnica

## Introducción
- Plataforma monolítica modular orientada a la gestión de instalaciones, equipos, reservas y mantenimientos universitarios.
- Construida con Java 17 y Spring Boot 3.5.x; despliega API REST JSON con seguridad basada en JWT y control fino de permisos por entidad.
- Contextos principales: Catálogos geográficos (Site), Inventario (Equipment), Reservas (Booking), Mantenimientos (Maintenance), Gestión de usuarios/permisos (User) y Notificaciones.
- Código fuente organizado bajo `src/main/java/com/corhuila/sgie`, con recursos de configuración en `src/main/resources` y documentación base en `docs/`.

## Arquitectura
- **Capas y patrones**: Controllers delegan en Services y estos en Repositories, siguiendo el patrón Onion; clases base `BaseController` y `BaseService` centralizan CRUD y soft-delete (`src/main/java/com/corhuila/sgie/common/BaseController.java:9`, `.../BaseService.java:12`).
- **Auditoría transversal**: entidades heredan de `Auditoria` (`.../common/Auditoria.java:10`) y `AuditoriaListener` registra usuarios mediante el `SecurityContextHolder` (`.../common/AuditoriaListener.java:11`).
- **Componentes comunes**: `ApiResponseDto`, `GlobalExceptionHandler`, `PermissionEvaluator`, vistas Jackson (`common` package) y utilitarios de reporting.
- **Modularización**: cada sub-paquete (`User`, `Site`, `Equipment`, `Booking`, `Maintenance`, `Notification`, `Security`, `Config`, `common`) agrupa entidades, DTOs, repos y servicios del dominio correspondiente.
- **Logging y observabilidad**: filtro `RequestLoggingConfig` añade `X-Request-Id` y mediciones por petición (`.../Config/RequestLoggingConfig.java:21`); Actuator habilitado para `health` e `info`.

## Seguridad
- **Cadena de filtros**: `SecurityConfig` (`.../Config/SecurityConfig.java:31`) configura CSRF con cookie `XSRF-TOKEN`, CORS parametrizable y sesión stateless; añade `JwtFilter` antes de `UsernamePasswordAuthenticationFilter` y `CsrfCookieFilter` después de `CsrfFilter`.
- **Autenticación**: `CustomUserDetailsService` carga usuarios, rol principal y permisos dinámicos `ENTIDAD:ACCION` desde `PermisoRolEntidad` (`.../Security/CustomUserDetailsService.java:22`). Passwords se almacenan con BCrypt fuerza 12 (`SecurityConfig`).
- **JWT**: `JwtUtil` firma tokens HS256 con `jwt.secret` (`.../Security/JwtUtil.java:17`). El filtro `JwtFilter` soporta cookie configurable (`security.jwt.cookie.*`) y header `Authorization` (`.../Security/JwtFilter.java:17`). El ID de usuario se guarda en `Authentication.details` para auditoría.
- **End-points públicos**: `/v1/api/usuario/login`, `/v1/api/usuario/me`, `/swagger-ui/**`, `/v3/api-docs/**`, `/api/equipos/reportes/**`. Logout limpia cookie JWT.
- **Autorización**: anotaciones `@PreAuthorize` usan `PermissionEvaluator` (`.../common/PermissionEvaluator.java:8`). Roles se exponen con prefijo `ROLE_`; permisos dinámicos habilitan o bloquean CRUD genérico y endpoints custom.
- **CSRF**: `CsrfCookieFilter` (`.../Security/CsrfCookieFilter.java:22`) expone token en cookie `XSRF-TOKEN` (no httpOnly) y header `X-XSRF-TOKEN` para clientes SPA.
- **Errores**: `GlobalExceptionHandler` mapea validaciones (400), auth (401), permisos (403) y fallos genéricos (500) (`.../common/GlobalExceptionHandler.java:16`).

## Módulos funcionales

### User
**Responsabilidad**: administración de personas, usuarios, roles, entidades y permisos; autenticación/authz.

| Componente | Descripción | Ruta |
|------------|-------------|------|
| `UsuarioController` | CRUD, login/logout, info del usuario autenticado | `src/main/java/com/corhuila/sgie/User/Controller/UsuarioController.java:33` |
| `UsuarioService` | Registra usuario cifrando password y validando unicidad persona | `.../User/Service/UsuarioService.java:18` |
| `PermisoRolEntidadService` | Gestiona permisos por rol/entidad y proyecciones | `.../User/Service/PermisoRolEntidadService.java:9` |
| Entidades clave | `Persona`, `Usuario`, `Rol`, `Permiso`, `Entidad`, `PermisoRolEntidad` | `.../User/Entity/*.java` |

**Endpoints destacados**

| Método | URI | Descripción | Permiso | Implementación |
|--------|-----|-------------|---------|----------------|
| POST | `/v1/api/usuario/login` | Autentica, emite JWT en cookie y cuerpo | Público | `UsuarioController`: `111` |
| GET | `/v1/api/usuario/me` | Devuelve identidad + roles/permisos vigentes | Público (requiere token) | `...UsuarioController.java:126` |
| POST | `/v1/api/usuario` | Alta de usuario ligado a persona | `USUARIO:CREAR` | `...UsuarioController.java:75` |
| GET | `/v1/api/persona/persona-usuario` | Consulta persona+usuario por identificación | `PERSONA:CONSULTAR` | `...PersonaController.java:18` |
| GET | `/v1/api/permiso-rol-entidad/persona-permisos-rol-entidad` | Permisos activos por persona | `PERMISO_ROL_ENTIDAD:CONSULTAR` | `...PermisoRolEntidadController.java:17` |

### Site
**Responsabilidad**: catálogos de ubicación (continente → país → departamento → municipio), campus e instalaciones.

| Componente | Descripción | Ruta |
|------------|-------------|------|
| `InstalacionController` | CRUD + consulta campus e informes | `.../Site/Controller/InstalacionController.java:30` |
| `InstalacionService` | Búsquedas y streaming CSV/XLSX/PDF | `.../Site/Service/InstalacionService.java:9` |
| `IInstalacionRepository` | Proyecciones nativas y JPQL para reportes | `.../Site/IRepository/IInstalacionRepository.java:12` |
| Entidades | `Continente`, `Pais`, `Departamento`, `Municipio`, `Campus`, `Instalacion` | `.../Site/Entity/*.java` |

**Endpoints destacados**

| Método | URI | Descripción | Permiso | Implementación |
|--------|-----|-------------|---------|----------------|
| GET | `/v1/api/pais/por-continente/{id}` | Países activos de un continente | `PAIS:CONSULTAR` | `.../Site/Controller/PaisController.java:18` |
| GET | `/v1/api/departamento/por-pais/{id}` | Departamentos por país | `DEPARTAMENTO:CONSULTAR` | `.../DepartamentoController.java:18` |
| GET | `/v1/api/instalacion/instalacion-campus` | Vista instalación ↔ campus | `INSTALACION:CONSULTAR` | `.../InstalacionController.java:41` |
| GET | `/v1/api/instalacion/reporte/{formato}` | Reporte en CSV/XLSX/PDF | `INSTALACION:CONSULTAR` | `.../InstalacionController.java:57` |

### Equipment
**Responsabilidad**: inventario de equipos, categorías y tipos, hoja de vida.

| Componente | Descripción | Ruta |
|------------|-------------|------|
| `EquipoController` | CRUD, consulta de equipos por instalación y hoja de vida | `.../Equipment/Controller/EquipoController.java:29` |
| `EquipoService` | Reportes y filtros nativos | `.../Equipment/Service/EquipoService.java:9` |
| `HojaDeVidaEquipoService` | Consolida datos del equipo + historial reservas/mantenimientos | `.../Equipment/Service/HojaDeVidaEquipoService.java:12` |
| Entidades | `CategoriaEquipo`, `TipoEquipo`, `Equipo` | `.../Equipment/Entity/*.java` |

**Endpoints destacados**

| Método | URI | Descripción | Permiso | Implementación |
|--------|-----|-------------|---------|----------------|
| GET | `/v1/api/equipo/equipo-instalacion` | Lista equipos filtrados por código/instalación | `EQUIPO:CONSULTAR` | `.../EquipoController.java:45` |
| GET | `/v1/api/equipo/hoja-vida-equipo/{id}` | Hoja de vida (reservas+mantenimientos) | `EQUIPO:CONSULTAR` | `.../EquipoController.java:52` |
| GET | `/v1/api/equipo/reporte/{formato}` | Exportación CSV/XLSX/PDF | `EQUIPO:CONSULTAR` | `.../EquipoController.java:59` |

### Booking
**Responsabilidad**: reservas de instalaciones y equipos, detalles y disponibilidad horaria.

| Componente | Descripción | Ruta |
|------------|-------------|------|
| `ReservaController` | CRUD, disponibilidad, reportes y vista consolidada | `.../Booking/Controller/ReservaController.java:30` |
| `ReservaService` | Validación anti-solapamiento, streams para reportes | `.../Booking/Service/ReservaService.java:16` |
| `DetalleReservaEquipoService` / `InstalacionService` | Gestión de detalles, validaciones de agenda y notificaciones | `.../Booking/Service/DetalleReservaEquipoService.java:19`, `.../Booking/Service/DetalleReservaInstalacionService.java:25` |
| Entidades | `Reserva`, `DetalleReservaEquipo`, `DetalleReservaInstalacion`, `TipoReserva` | `.../Booking/Entity/*.java` |

**Endpoints destacados**

| Método | URI | Descripción | Permiso | Implementación |
|--------|-----|-------------|---------|----------------|
| GET | `/v1/api/reserva/horas-disponibles-instalacion` | Horas libres por instalación/fecha | `RESERVA:CONSULTAR` | `.../ReservaController.java:44` |
| GET | `/v1/api/reserva/horas-disponibles-equipo` | Horas libres por equipo | `RESERVA:CONSULTAR` | `.../ReservaController.java:54` |
| GET | `/v1/api/reserva/reservas-mantenimientos` | Historial reservas+mantenimientos por persona | `RESERVA:CONSULTAR` | `.../ReservaController.java:64` |
| PUT | `/v1/api/detalle-reserva-equipo/{idDetalle}/cerrar-detalle-reserva-equipo` | Cierre de reserva de equipo | `DETALLE_RESERVA_EQUIPO:ACTUALIZAR` | `.../DetalleReservaEquipoController.java:26` |
| PUT | `/v1/api/detalle-reserva-instalacion/{idDetalle}/actualizar-detalle-reserva` | Actualización con validación de agenda | `DETALLE_RESERVA_INSTALACION:ACTUALIZAR` | `.../DetalleReservaInstalacionController.java:28` |

### Maintenance
**Responsabilidad**: planeación y seguimiento de mantenimientos de equipos e instalaciones.

| Componente | Descripción | Ruta |
|------------|-------------|------|
| `MantenimientoEquipoController` / `Service` | CRUD, cierre y actualización con validación de horarios | `.../Maintenance/Controller/MantenimientoEquipoController.java:13`, `.../Maintenance/Service/MantenimientoEquipoService.java:19` |
| `MantenimientoInstalacionController` / `Service` | Similar para instalaciones | `.../Maintenance/Controller/MantenimientoInstalacionController.java:13`, `.../Maintenance/Service/MantenimientoInstalacionService.java:19` |
| Categorías | Entidades y servicios `CategoriaMantenimientoEquipo/Instalacion` | `.../Maintenance/Entity/*.java` |

**Endpoints destacados**

| Método | URI | Descripción | Permiso | Implementación |
|--------|-----|-------------|---------|----------------|
| PUT | `/v1/api/mantenimiento-equipo/{id}/cerrar-mantenimiento-equipo` | Cierra mantenimiento y reserva asociada | `MANTENIMIENTO_EQUIPO:ACTUALIZAR` | `.../MantenimientoEquipoController.java:26` |
| PUT | `/v1/api/mantenimiento-instalacion/{id}/actualizar-mantenimiento-instalacion` | Actualiza datos y verifica disponibilidad | `MANTENIMIENTO_INSTALACION:ACTUALIZAR` | `.../MantenimientoInstalacionController.java:28` |
| GET | `/v1/api/mantenimiento-instalacion/mantenimientos-instalaciones` | Historial por persona | `MANTENIMIENTO_INSTALACION:CONSULTAR` | `.../MantenimientoInstalacionController.java:38` |

### Notification
- `NotificacionService` envía correos vía SMTP (`.../Notification/NotificacionService.java:9`).
- `NotificacionDiariaService` programa recordatorio diario 23:59 zona Bogotá para reservas activas (`.../Notification/NotificacionDiariaService.java:11`).
- Servicios de Booking y Maintenance disparan correos en `afterSave` con plantillas específicas.

## Persistencia y entidades
- Repositorios heredan de `IBaseRepository` (`.../common/IBaseRepository.java:9`), soporte Spring Data JPA.
- Soft-delete: `BaseService.delete` marca `state=false` y `deletedAt` (`.../common/BaseService.java:85`).
- Proyecciones: uso extensivo de interfaces (ej. `IReservaGeneralDTO`, `IInstalacionCampusDTO`, `IEquipoInstalacionDTO`) en queries nativas/JPQL para respuestas enriquecidas.
- Auditoría de usuarios se alimenta del `Authentication.details` (ID) fijado en `JwtFilter`.
- Scripts de disponibilidad dependen de funciones nativas Postgres `horas_disponibles_instalacion/equipo`.

---

### `horas_disponibles_equipo(p_fecha DATE, p_idEquipo INT, p_idDetalle BIGINT DEFAULT NULL)`

- **Objetivo:** devolver las franjas horarias libres (saltos de 1 h entre 05:00 y 21:00) para un equipo específico en la fecha indicada.  
- **Lógica:**
  - Genera todas las horas posibles con `generate_series`.
  - Construye la CTE `ocupadas` uniendo reservas activas y mantenimientos de equipos (`COALESCE(state, false) = true`).
  - Excluye las horas ocupadas; en actualizaciones, `p_idDetalle` permite ignorar el detalle/mantenimiento en edición gracias a `IS DISTINCT FROM`.
- **Uso recomendado en servicios:** `DetalleReservaEquipoService` y `MantenimientoEquipoService` deben consultar esta función para validar disponibilidad antes de crear/actualizar.
- **Ejemplo de consulta:**
  ```sql
  SELECT *
  FROM horas_disponibles_equipo('2024-11-20', 42, NULL);

### `horas_disponibles_instalacion(p_fecha DATE, p_idInstalacion INT, p_idDetalle BIGINT DEFAULT NULL)`

- **Objetivo:** calcular las franjas horarias libres (saltos de 1 h entre 05:00 y 21:00) para una instalación específica en la fecha indicada, considerando reservas y mantenimientos vigentes.
- **Lógica:**
  - Genera todas las horas posibles con `generate_series`.
  - Construye la CTE `ocupadas` uniendo reservas activas de instalaciones y mantenimientos (`COALESCE(state, false) = true`).
  - Excluye las horas ocupadas; `p_idDetalle` permite omitir un detalle concreto (por ejemplo, al actualizar un mantenimiento existente) mediante `IS DISTINCT FROM`.
- **Uso recomendado en servicios:** `DetalleReservaInstalacionService` y `MantenimientoInstalacionService` deben consultar esta función para validar disponibilidad antes de crear o actualizar registros.
- **Ejemplo de consulta:**
  ```sql
  SELECT *
  FROM horas_disponibles_instalacion('2024-11-20', 17, NULL);

## Reporting
- `ReporteGenericoService` ofrece generación en memoria o streaming (`.../common/Reporting/ReporteGenericoService.java:20`).
- `GeneradorReporteUtil` selecciona `CsvReportWriter`, `XlsxReportWriter` u `PdfReportWriter` según `ReportFormat` (`.../common/Reporting/GeneradorReporteUtil.java:18`).
- Controladores de Site, Equipment y Booking exponen `/reporte/{formato}` (CSV/XLSX/PDF). Acceso abierto sin token para `/api/equipos/reportes/**` (configurable).
- Helper `HelperUtils` arma headers y normaliza parámetros (`.../common/Reporting/HelperUtils.java:7`).

## Configuración y DevOps
- Dependencias principales declaradas en `pom.xml` (`pom.xml:36`) incluyen Spring (web, security, data), JWT, MapStruct, Spring Mail, Apache POI, OpenPDF y Jacoco.
- Perfiles `dev`, `qa`, `prod` definen credenciales y políticas (DDL auto, logging) (`src/main/resources/application-dev.yml:1`, `...-qa.yml:1`, `...-prod.yml:1`).
- Configuración base (`application.yml:1`) fuerza lectura de secretos vía variables de entorno (`JWT_SECRET`, `MAIL_*`, `*_DB_*`), controla cookies de sesión y JWT, y personaliza logs con `requestId`.
- Testing usa H2 en modo PostgreSQL (`src/test/resources/application-test.yml:1`).
- Actuator expone `health`, `info` (prod añade `metrics`).

## Testing
- Cobertura amplia de unidades/controladores en `src/test/java`, destacando:
  - Seguridad: `JwtFilterTest`, `CustomUserDetailsServiceTest`, `CsrfCookieFilterTest` (`.../Security/*.java`).
  - Servicios de reservas/mantenimientos/equipos (`.../Booking/Service/ReservaServiceTest.java:1`, `.../Maintenance/Service/MantenimientoEquipoServiceTest.java:1`, `.../Equipment/Service/HojaDeVidaEquipoServiceTest.java:1`).
  - Controllers REST con `@WebMvcTest`.
  - Reporting (`.../common/Reporting/GeneradorReporteUtilTest.java:1`) y capas comunes (`BaseServiceTest`, `GlobalExceptionHandlerTest`).
- Jacoco configurado con reglas flexibles (sin mínimo de cobertura) pero excluyendo DTOs/entities.

## Cómo extender el backend
1. **Modelado**: crear entidad que extienda `Auditoria` y relaciones necesarias.
2. **Repositorio**: implementar interfaz extendiendo `IBaseRepository`.
3. **Servicio**: extender `BaseService`, inyectar repositorio y sobrescribir `beforeSave/afterSave` o validaciones necesarias.
4. **Controlador**: extender `BaseController` para CRUD básico; añadir endpoints específicos y anotar con `@PreAuthorize`.
5. **Permisos**: registrar nueva `Entidad` y asociar `Permiso`s vía `PermisoRolEntidad` para roles pertinentes.
6. **DTOs/Proyecciones**: definir interfaces o clases para respuestas custom; mapear en repositorios.
7. **Documentar/reportar**: si requiere exportes, agregar DTO de reporte y reusar `ReporteGenericoService`.
8. **Testing**: crear pruebas unitarias/mocked para servicios y controladores; añadir casos de error.
9. **Config**: documentar variables adicionales y, si aplica, exponer en Swagger mediante anotaciones.

## Guía para consumidores del API
- **Autenticación**: enviar POST `/v1/api/usuario/login` con JSON `{ "email": "...", "password": "..." }`. Respuesta incluye token JWT y cookie `token` (httpOnly). Ampliar sesión reenviando cookie + header `X-XSRF-TOKEN` (valor del header o cookie homónima).
- **Sesiones**: el backend es stateless; cada request autenticada debe incluir cookie JWT o header `Authorization: Bearer <token>`. Para SPA usar cookie + header CSRF.
- **Permisos**: la mayoría de rutas requieren que el usuario tenga `ENTIDAD:ACCION` (ver 403). `ApiResponseDto` provee campos `message`, `data`, `status`.
- **Errores**: validaciones devuelven 400 con mapa de errores; credenciales inválidas 401; faltas de permiso 403.
- **Reportes**: endpoints `/reporte/{formato}` devuelven `application/octet-stream` (stream). Param `modo=memory` permite carga previa antes de descargar.
- **Versionado/Docs**: Swagger en `/swagger-ui/index.html` con esquema bearer. Actuator `GET /actuator/health`.
- **Buenas prácticas cliente**: propagar `X-Request-Id` para correlación; manejar expiración de cookie (valor configurable en propiedades); cerrar sesión vía POST `/v1/api/usuario/logout`.

## Glosario y referencias
- Configuración de seguridad: `src/main/java/com/corhuila/sgie/Config/SecurityConfig.java:31`
- Filtro JWT: `src/main/java/com/corhuila/sgie/Security/JwtFilter.java:17`
- Evaluador de permisos: `src/main/java/com/corhuila/sgie/common/PermissionEvaluator.java:8`
- Servicio de reporting: `src/main/java/com/corhuila/sgie/common/Reporting/ReporteGenericoService.java:20`
- Controlador de reservas: `src/main/java/com/corhuila/sgie/Booking/Controller/ReservaController.java:30`
- Servicio de hoja de vida de equipos: `src/main/java/com/corhuila/sgie/Equipment/Service/HojaDeVidaEquipoService.java:12`
- Servicio de notificaciones: `src/main/java/com/corhuila/sgie/Notification/NotificacionService.java:9`
- Config general: `src/main/resources/application.yml:1`
- Script Jacoco y dependencias: `pom.xml:36`
