# Corhuila SGIE API

API REST en Java 17 con Spring Boot para gestionar instalaciones, equipos, reservas y mantenimientos de la Universidad Corhuila. Provee autenticación JWT, control de permisos por entidad y utilidades de reporting en CSV/XLSX/PDF.

## Funcionalidades

- Gestión de catálogos geográficos (continente → país → departamento → municipio), campus e instalaciones.
- Inventario de equipos con hoja de vida (reservas + mantenimientos).
- Reservas de instalaciones y equipos con validación de disponibilidad.
- Órdenes de mantenimiento e integración con reservas asociadas.
- Usuarios, roles y permisos dinámicos por entidad (`ENTIDAD:ACCION`).
- Envío de notificaciones por correo y tareas programadas.
- Exportes de datos en múltiples formatos (CSV, XLSX, PDF).

## Stack

- Java 17, Spring Boot 3.5.x (web, security, data-jpa, validation, actuator).
- PostgreSQL (producción) y H2 (tests).
- Spring Security con JWT (`jjwt` 0.11.5).
- Lombok y MapStruct (pendiente de uso).
- Apache POI y OpenPDF para reportes.
- Maven Wrapper (`./mvnw`).

## Requisitos

| Herramienta | Versión recomendada        |
|-------------|----------------------------|
| JDK         | 17 LTS                     |
| Maven       | Opcional (usa `./mvnw`)    |
| PostgreSQL  | 13 o superior              |

## Variables de entorno

Configura las siguientes variables antes de ejecutar la aplicación. Los perfiles (`dev`, `qa`, `prod`) toman cada valor mediante `application.yml` y sus overrides:

```
DEV_DB_URL=jdbc:postgresql://localhost:5432/sgie_dev
DEV_DB_USERNAME=postgres
DEV_DB_PASSWORD=postgres

QA_DB_URL=...
PROD_DB_URL=...

JWT_SECRET=clave_segura_de_64_bytes
JWT_EXPIRATION_MS=21600000

JWT_COOKIE_NAME=token
JWT_COOKIE_SECURE=false
JWT_COOKIE_SAMESITE=None
JWT_COOKIE_MAX_AGE=3600

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=usuario@example.com
MAIL_PASSWORD=clave
```

> Ajusta los valores según tu entorno. Puedes cargarlos con un archivo `.env` o exportarlos en tu shell.

## Puesta en marcha

```bash
# Compilar el proyecto
./mvnw clean package

# Ejecutar en el perfil por defecto (dev)
./mvnw spring-boot:run

# Ejecutar en otro perfil (ej. qa)
SPRING_PROFILES_ACTIVE=qa ./mvnw spring-boot:run
```

La API quedará disponible en `http://localhost:8080`.

## Endpoints útiles

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Actuator health: `http://localhost:8080/actuator/health`

## Tests y calidad

```bash
# Ejecutar suite de pruebas
./mvnw test

# Generar reporte de cobertura (Jacoco)
./mvnw verify
```

El informe de cobertura se genera en `target/site/jacoco/index.html`.

## Documentación

Consulta la guía técnica completa en [`docs/backend.md`](docs/backend.md). Incluye arquitectura, módulos, seguridad, funciones SQL de disponibilidad y lineamientos para extender el backend.

## Contribuir

1. Crea una rama (`feature/...` o `fix/...`).
2. Implementa tus cambios y añade pruebas.
3. Ejecuta `./mvnw test`.
4. Abre un Pull Request describiendo el impacto y pasos de validación.

## Licencia

Indica aquí la licencia del proyecto (MIT, GPL, privada, etc.). Si aún no se define, documéntalo en este apartado.
