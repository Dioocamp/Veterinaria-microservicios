# GUÍA AVANZADA — API Gateway, Seguridad JWT, Swagger, Pruebas y Docker

> Complemento de [EXPLICACION_CODIGO.md](EXPLICACION_CODIGO.md). Cubre las piezas añadidas para
> cumplir las rúbricas: API Gateway, autenticación JWT, documentación Swagger/OpenAPI, pruebas
> unitarias y despliegue con Docker.
> Autor: **Dinko Ocampo**

---

## 1. MAPA COMPLETO DE PUERTOS

| Componente | Puerto | Base de datos | Rol |
|------------|--------|---------------|-----|
| eureka-server | 8761 | — | Descubrimiento de servicios |
| **api-gateway** | **8080** | — | Puerta de entrada única + seguridad JWT |
| **ms-auth** | **8089** | `vet_auth` | Login y emisión de tokens JWT |
| ms-tutores | 8081 | `vet_tutores` | Maestro |
| ms-pacientes | 8082 | `vet_pacientes` | → ms-tutores |
| ms-especialidades | 8083 | `vet_especialidades` | Maestro |
| ms-profesionales | 8084 | `vet_profesionales` | → ms-especialidades |
| ms-procedimientos | 8085 | `vet_procedimientos` | → ms-pacientes, ms-profesionales |
| ms-servicios | 8086 | `vet_servicios` | Maestro |
| ms-categorias | 8087 | `vet_categorias` | Maestro |
| ms-productos | 8088 | `vet_productos` | → ms-categorias |

---

## 2. API GATEWAY (Spring Cloud Gateway)

El gateway (`api-gateway`, puerto **8080**) es el **único punto de entrada**. En vez de llamar a cada
microservicio por su puerto, todo entra por `http://localhost:8080/<servicio>/...`. El gateway:

1. Resuelve el destino por **nombre lógico** vía Eureka (`uri: lb://ms-xxx`).
2. Quita el prefijo del servicio con el filtro `StripPrefix=1`.
   Ej: `GET /ms-tutores/api/v1/tutores` → reenvía a ms-tutores como `/api/v1/tutores`.
3. Aplica el filtro de seguridad `AuthenticationFilter` (valida el JWT y, si la ruta lo exige, el rol).

### Reglas de enrutamiento y seguridad (en `api-gateway/src/main/resources/application.yml`)

| Ruta de entrada (gateway) | Servicio destino | Requiere |
|---------------------------|------------------|----------|
| `/ms-auth/auth/**` | ms-auth | Público (sin token) |
| `/ms-tutores/**` | ms-tutores | Token con **ROLE_ADMIN** |
| `/ms-pacientes/**` | ms-pacientes | Token con **ROLE_TUTOR** |
| `/ms-profesionales/**` | ms-profesionales | Token con **ROLE_PROFESIONAL** |
| `/ms-especialidades/**` | ms-especialidades | Público (sin token) |
| `/ms-procedimientos/**` | ms-procedimientos | Público (sin token) |
| `/ms-servicios/**` | ms-servicios | Público (sin token) |
| `/ms-categorias/**` | ms-categorias | Público (sin token) |
| `/ms-productos/**` | ms-productos | Público (sin token) |

La **seguridad por rol se aplica solo a tutores, pacientes y profesionales**. El resto de los
servicios se enrutan por el gateway pero quedan abiertos (sin token). La **documentación Swagger** y
el **despliegue Docker** sí están en **todos** los microservicios.

Respuestas del filtro: **401** si no hay token o es inválido/expirado; **403** si el token es válido
pero no tiene el rol requerido.

---

## 3. AUTENTICACIÓN JWT (ms-auth)

`ms-auth` (puerto **8089**, BD `vet_auth`) gestiona usuarios y roles, y emite tokens **JWT** firmados
con HMAC-SHA256. El gateway valida esos tokens con el **mismo secreto** (`jwt.secret`).

### Usuarios de prueba (se crean solos al arrancar, ver `DataSeeder`)

| Usuario | Password | Rol |
|---------|----------|-----|
| `admin` | `admin` | ROLE_ADMIN |
| `tutor` | `tutor` | ROLE_TUTOR |
| `profesional` | `profesional` | ROLE_PROFESIONAL |

### Login

`POST http://localhost:8089/auth/login` (o por el gateway: `POST http://localhost:8080/ms-auth/auth/login`)

```json
{
  "username": "admin",
  "password": "admin"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tipo": "Bearer",
  "username": "admin"
}
```

### Usar el token

En las siguientes peticiones al gateway, agregar el header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9....
```

### Flujo completo de ejemplo (probado y funcionando)

1. `POST http://localhost:8080/ms-auth/auth/login` con `admin/admin` → copiar `token`.
2. `GET http://localhost:8080/ms-tutores/api/v1/tutores` con `Authorization: Bearer <token>` → **200**.
3. Sin el header → **401**. Con un token de `tutor` (ROLE_TUTOR) a esa misma ruta → **403**.

> Nota: las contraseñas se guardan en texto plano por simplicidad académica. En producción se usaría
> `BCryptPasswordEncoder`.

---

## 4. DOCUMENTACIÓN SWAGGER / OPENAPI

Cada microservicio expone su documentación interactiva con **springdoc-openapi** (Swagger UI).
Con el servicio levantado:

| Servicio | Swagger UI | OpenAPI JSON |
|----------|-----------|--------------|
| ms-tutores | http://localhost:8081/swagger-ui/index.html | http://localhost:8081/v3/api-docs |
| ms-pacientes | http://localhost:8082/swagger-ui/index.html | http://localhost:8082/v3/api-docs |
| ms-especialidades | http://localhost:8083/swagger-ui/index.html | http://localhost:8083/v3/api-docs |
| ms-profesionales | http://localhost:8084/swagger-ui/index.html | http://localhost:8084/v3/api-docs |
| ms-procedimientos | http://localhost:8085/swagger-ui/index.html | http://localhost:8085/v3/api-docs |
| ms-servicios | http://localhost:8086/swagger-ui/index.html | http://localhost:8086/v3/api-docs |
| ms-categorias | http://localhost:8087/swagger-ui/index.html | http://localhost:8087/v3/api-docs |
| ms-productos | http://localhost:8088/swagger-ui/index.html | http://localhost:8088/v3/api-docs |
| ms-auth | http://localhost:8089/swagger-ui/index.html | http://localhost:8089/v3/api-docs |

Swagger genera automáticamente la lista de endpoints, los modelos de datos (DTOs), los parámetros y
los códigos de respuesta. El título y la descripción de cada API se definen en la clase
`OpenApiConfig` de cada servicio.

---

## 5. PRUEBAS UNITARIAS (JUnit 5 + Mockito)

Cada microservicio tiene pruebas unitarias de su capa **Service** en `src/test/java`, usando
**JUnit 5** y **Mockito** (mockeando el repositorio y, donde aplica, el cliente Feign).

Cubren: listar, obtener por id, **recurso no encontrado (404)**, crear, **duplicado (409)** y, en los
servicios que consumen otros, **referencia externa inexistente (400)**.

Ejecutar todas las pruebas:

```bash
mvn test
```

Resultado actual: **41 pruebas, 0 fallos** en los 8 microservicios.

---

## 6. DESPLIEGUE CON DOCKER

Cada componente tiene su `Dockerfile` y hay un `docker-compose.yml` en la raíz que levanta MySQL +
Eureka + Gateway + ms-auth + los 8 microservicios en una red interna `veterinaria-net`.

### Pasos

1. Compilar los JAR (los `Dockerfile` copian `target/*.jar`):
   ```bash
   mvn clean package -DskipTests
   ```
2. Construir y levantar todo:
   ```bash
   docker compose up --build
   ```
3. Apagar:
   ```bash
   docker compose down
   ```

### Notas

- Dentro de Docker, los servicios se comunican por **nombre de contenedor**, no por `localhost`. Esto
  se inyecta con **variables de entorno** en `docker-compose.yml` (`SPRING_DATASOURCE_URL` apunta a
  `mysql`, `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` apunta a `eureka-server`), sin tocar el código.
- Se mantiene **una base de datos por microservicio** (`vet_<nombre>`) dentro del contenedor MySQL,
  creadas automáticamente con `createDatabaseIfNotExist=true`.
- MySQL tiene `healthcheck`; los microservicios esperan a que la base esté lista (`depends_on`).
- Requiere tener **Docker Desktop** instalado y corriendo.

---

## 7. RESUMEN DE CUMPLIMIENTO DE RÚBRICAS

| Indicador | Cómo se cumple |
|-----------|----------------|
| IE 1.2.1 (CSR por capas) | controller / service(+impl) / repository / entity / dto / exception |
| IE 2.2.1 (reglas e integridad) | validaciones `@Valid`, unicidad, validación de referencias por Feign |
| IE 2.4.1 (REST entre MS) | OpenFeign + manejo de códigos HTTP (200/201/400/404/409) |
| IE 3.1.1 (pruebas unitarias) | 41 pruebas JUnit 5 + Mockito sobre la capa Service |
| IE 3.2.1 (Swagger/OpenAPI) | springdoc en cada MS, Swagger UI + `/v3/api-docs` |
| IE 3.3.1 (despliegue) | Dockerfiles + docker-compose (y ejecución local verificada) |
| IE 3.3.2 (API Gateway) | Spring Cloud Gateway centraliza el enrutamiento |
| IE 3.3.3 (interoperabilidad) | rutas, `StripPrefix` y filtro de autenticación por rol |
| IE 3.3.4 (YAML/perfiles/env) | `application.yml` del gateway + variables de entorno en compose |

> IE 2.5.1 (commits distribuidos entre integrantes) e IE 2.5.2 (Trello) son de **gestión de equipo**:
> dependen de que cada integrante haga sus commits y de usar Trello; no se resuelven desde el código.
