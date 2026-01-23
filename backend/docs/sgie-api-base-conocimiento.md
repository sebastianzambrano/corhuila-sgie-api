# Corhuila SGIE API · Base de Conocimiento

Este documento complementa al [`README.md`](../README.md) y a la guía técnica principal [`docs/backend.md`](backend.md). Sirve como ficha ejecutiva: describe el propósito del backend, su arquitectura general y localiza la documentación detallada.

## Resumen ejecutivo

- **Objetivo:** plataforma REST para gestionar instalaciones, equipos, reservas y mantenimientos de la Universidad Corhuila con control de usuarios, roles y permisos finos por entidad.
- **Dominio:** catálogos geográficos (continente → país → departamento → municipio), campus, instalaciones, inventario de equipos, reservas, mantenimientos y notificaciones.
- **Tecnologías clave:** Java 17, Spring Boot 3.5.x, Spring Security + JWT, Spring Data JPA, PostgreSQL, Spring Mail, Apache POI, OpenPDF.
- **Estilo de API:** REST JSON bajo `/v1/api`, responses envueltas en `ApiResponseDto` salvo endpoints especiales (`/usuario/login`, `/usuario/me`, reportes streaming).

## Stack y despliegue

| Componente          | Detalle                                                                   |
|---------------------|---------------------------------------------------------------------------|
| Lenguaje/Runtime    | Java 17                                                                   |
| Framework           | Spring Boot (web, security, data-jpa, validation, actuator)               |
| Seguridad           | JWT (jjwt 0.11.5), filtros custom (`JwtFilter`, `CsrfCookieFilter`)       |
| Persistencia        | PostgreSQL (dev/qa/prod), H2 en `application-test.yml`                    |
| Build               | Maven Wrapper (`./mvnw`)                                                  |
| Reporting           | Apache POI (XLSX), OpenPDF, generación centralizada en `ReporteGenericoService` |
| Infra adicional     | Spring Mail, tareas programadas (`@EnableScheduling`)                     |

Variables de entorno, comandos de ejecución y enlaces útiles se describen en el [`README.md`](../README.md#variables-de-entorno).

## Arquitectura a alto nivel

- **Monolito modular con capas claras:**
  - `Controller` → expone REST (la mayoría extiende `BaseController` para CRUD + permisos).
  - `Service` → lógica de negocio; todos extienden `BaseService` (soft delete, hooks `before/afterSave`).
  - `Repository` → interfaces `IBaseRepository` (Spring Data JPA, queries JPQL/nativas, proyecciones mediante interfaces DTO).
  - `common` → infraestructura compartida (auditoría, manejo de errores, evaluador de permisos, reporting).
- **Auditoría transversal:** entidades heredan de `Auditoria`; `AuditoriaListener` agrega `createdUser`, `updatedUser`, `deletedUser` usando el `SecurityContext`.
- **Seguridad:** `SecurityConfig` configura cadena stateless, CORS, CSRF con cookie exponible, rutas públicas (`/v1/api/usuario/login`, `/v1/api/usuario/me`, Swagger). `CustomUserDetailsService` arma authorities combinando rol (`ROLE_*`) + permisos dinámicos (`ENTIDAD:ACCION`).
- **Notificaciones:** `NotificacionService` (correo) y `NotificacionDiariaService` (job 23:59 America/Bogota) integran recordatorios de reservas.

La descripción exhaustiva de cada paquete y endpoint está en [`docs/backend.md`](backend.md).

## Módulos funcionales (visión rápida)

| Módulo       | Responsabilidad principal                                   | Paquete raíz                             |
|--------------|-------------------------------------------------------------|-------------------------------------------|
| `User`       | Personas, usuarios, roles, permisos, autenticación/login    | `com.corhuila.sgie.User`                  |
| `Site`       | Geografía, campus, instalaciones y reportes asociados       | `com.corhuila.sgie.Site`                  |
| `Equipment`  | Inventario de equipos, hoja de vida, reportes               | `com.corhuila.sgie.Equipment`             |
| `Booking`    | Reservas de instalaciones/equipos y validación de horarios | `com.corhuila.sgie.Booking`               |
| `Maintenance`| Mantenimientos de equipos/instalaciones vinculados a reservas | `com.corhuila.sgie.Maintenance`        |
| `Notification` | Envío de correos y tareas programadas                    | `com.corhuila.sgie.Notification`          |
| `Security`   | JWT, filtros, servicios de usuario                          | `com.corhuila.sgie.Security`              |
| `common`     | Auditoría, reporting, controladores base, manejo de errores | `com.corhuila.sgie.common`                |

## Seguridad y permisos

- Autenticación mediante login (`/v1/api/usuario/login`) → genera JWT y cookie `token`.
- Endpoints protegidos con `@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACCION')")`.
- `JwtFilter` admite token vía cookie o header `Authorization: Bearer`.
- Token enlaza authorities con formato `ROLE_*` y `ENTIDAD:ACCION` para permisos finos.
- `CsrfCookieFilter` expone token CSRF en `XSRF-TOKEN` y header `X-XSRF-TOKEN` para clientes SPA.
- Manejo de errores centralizado: `GlobalExceptionHandler` responde con `ApiResponseDto` (400, 401, 403, 500).

## Disponibilidad y horarios

Las funciones Postgres `horas_disponibles_equipo` y `horas_disponibles_instalacion` generan horas libres. Su documentación completa (objetivo, lógica y ejemplos de uso) se encuentra en la sección “Funciones SQL de disponibilidad” de [`docs/backend.md`](backend.md#funciones-sql-de-disponibilidad).

## Referencias cruzadas

- **README:** visión general, requisitos, variables de entorno, comandos y enlaces rápidos.
- **docs/backend.md:** documentación técnica completa (arquitectura detallada, endpoints, DTOs, seguridad, reporting, extensibilidad, TODOs).
- **Este documento:** hoja de contexto ejecutiva que apunta a ambos recursos.

Mantén este markdown sincronizado cuando cambie la arquitectura o los módulos clave; para especificaciones profundas, actualiza siempre `docs/backend.md` y enlaza desde aquí.
