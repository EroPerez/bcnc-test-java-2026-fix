# bcnc-test-java-2026

Servicio REST desarrollado con **Spring Boot 3** que resuelve la consulta del precio aplicable a un producto en una fecha dada, aplicando prioridad de tarifas cuando varias se solapan en el tiempo.

---

## Tabla de contenidos

- [Descripción del problema](#descripción-del-problema)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Tecnologías](#tecnologías)
- [Endpoint](#endpoint)
- [Ejemplos de uso](#ejemplos-de-uso)
- [Ejecución](#ejecución)
- [Tests](#tests)
- [Datos de prueba](#datos-de-prueba)
- [Mejoras posibles](#mejoras-posibles)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)
- [Contacto](#contacto)

---

## Descripción del problema

En la base de datos de comercio electrónico de la compañía se dispone de la tabla `PRICES` que refleja el precio final (pvp) y la tarifa que aplica a un producto de una cadena entre unas fechas determinadas:

| BRAND_ID | START_DATE          | END_DATE            | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|---------------------|---------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1          | 35455      | 0        | 35.50 | EUR  |
| 1        | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2          | 35455      | 1        | 25.45 | EUR  |
| 1        | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3          | 35455      | 1        | 30.50 | EUR  |
| 1        | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4          | 35455      | 1        | 38.95 | EUR  |

**Campos:**

- `BRAND_ID`: foreign key de la cadena del grupo (1 = ZARA).
- `START_DATE`, `END_DATE`: rango de fechas en el que aplica la tarifa indicada.
- `PRICE_LIST`: identificador de la tarifa de precios aplicable.
- `PRODUCT_ID`: identificador código de producto.
- `PRIORITY`: desambiguador de aplicación de precios. Si dos tarifas coinciden en un rango de fechas, se aplica la de mayor prioridad (mayor valor numérico).
- `PRICE`: precio final de venta.
- `CURR`: ISO de la moneda.

El servicio expone un único endpoint `GET /api/prices` que, dados una marca, un producto y una fecha/hora, devuelve la tarifa vigente con mayor prioridad.

---

## Arquitectura

Se ha implementado una **arquitectura hexagonal** (puertos y adaptadores) con separación estricta de capas:

```
┌────────────────────────────────────────────────────────────────┐
│  Infraestructura                                               │
│  ┌───────────────┐  ┌──────────────────┐  ┌────────────────┐  │
│  │PriceController│  │PriceRepository   │  │BrandRepository │  │
│  │  (REST)       │  │Adapter (JPA+caché│  │Adapter (JPA)   │  │
│  └──────┬────────┘  └────────┬─────────┘  └───────┬────────┘  │
│         │                   │                     │            │
├─────────┼───────────────────┼─────────────────────┼───────────┤
│  Aplicación                 │                     │            │
│  ┌──────▼────────┐          │                     │            │
│  │  PriceService │          │                     │            │
│  │ (caso de uso) │          │                     │            │
│  └──────┬────────┘          │                     │            │
├─────────┼───────────────────┼─────────────────────┼───────────┤
│  Dominio│                   │                     │            │
│  ┌──────▼──────┐  ┌─────────▼────────┐  ┌────────▼─────────┐  │
│  │    Price    │  │PriceRepositoryPort│  │BrandRepositoryPort│  │
│  │  (modelo)   │  │    (puerto)       │  │    (puerto)       │  │
│  └─────────────┘  └──────────────────┘  └──────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

| Capa | Responsabilidad |
|------|----------------|
| **Dominio** | Modelos `Price` y `Brand`, puertos `PriceRepositoryPort` y `BrandRepositoryPort` |
| **Aplicación** | Caso de uso `PriceService` — orquesta la lógica sin dependencias de infraestructura |
| **Infraestructura** | Controlador REST, entidades JPA, adaptadores de repositorio (con caché Caffeine), mapeadores, manejador de errores, configuración |

La caché se gestiona en la capa de infraestructura (`PriceRepositoryAdapter`) manteniendo el núcleo de aplicación libre de dependencias técnicas.

---

## Estructura del proyecto

```
src/
├── main/java/es/bcnc/demo/
│   ├── DemoApplication.java                               # Punto de entrada (@EnableCaching)
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Price.java                                 # Modelo de dominio — precio
│   │   │   └── Brand.java                                 # Modelo de dominio — marca
│   │   └── port/
│   │       ├── PriceRepositoryPort.java                   # Puerto de salida — precios
│   │       └── BrandRepositoryPort.java                   # Puerto de salida — marcas
│   ├── application/
│   │   └── service/
│   │       ├── PriceService.java                          # Interfaz del caso de uso
│   │       └── impl/PriceServiceImpl.java                 # Implementación del caso de uso
│   └── infrastructure/
│       ├── rest/
│       │   ├── controller/PriceController.java            # Endpoint REST
│       │   └── dto/
│       │       ├── PriceRequest.java                      # DTO de entrada
│       │       └── PriceResponse.java                     # DTO de salida
│       ├── entity/
│       │   ├── PriceEntity.java                           # Entidad JPA — precio
│       │   └── BrandEntity.java                           # Entidad JPA — marca
│       ├── repository/
│       │   ├── JpaPriceRepository.java                    # Repositorio JPA + consulta JPQL (LIMIT 1)
│       │   ├── JpaBrandRepository.java                    # Repositorio JPA — marcas
│       │   ├── PriceRepositoryAdapter.java                # Adaptador + caché Caffeine
│       │   └── BrandRepositoryAdapter.java                # Adaptador — marcas
│       ├── mapper/PriceMapper.java                        # Conversión entidad ↔ dominio ↔ DTO
│       ├── exception/
│       │   ├── PriceNotFoundException.java                # Excepción precio no encontrado
│       │   ├── BrandNotFoundException.java                # Excepción marca no encontrada
│       │   └── GlobalExceptionHandler.java                # Manejador global de errores
│       └── config/OpenApiConfig.java                      # Configuración Swagger/OpenAPI
├── main/resources/
│   ├── application.properties
│   ├── schema.sql                                         # DDL — crea tablas al arrancar
│   └── data.sql                                           # DML — inserta datos de prueba
└── test/java/es/bcnc/demo/
    ├── DemoApplicationTests.java                          # Test de humo — contexto Spring
    ├── application/service/PriceServiceTest.java          # Tests unitarios del servicio
    └── infrastructure/
        ├── repository/JpaPriceRepositoryTest.java         # Tests de integración JPA
        ├── controller/PriceControllerIntegrationTest.java # Tests de integración Web (5 escenarios)
        └── exception/GlobalExceptionHandlerTest.java      # Tests del manejador de errores
```

---

## Tecnologías

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.5.13 | Framework base |
| Spring Data JPA | — | Acceso a datos |
| Spring Web | — | Capa REST |
| Spring Cache + Caffeine | — | Caché en memoria (infraestructura) |
| H2 Database | — | Base de datos en memoria (dev/test) |
| Lombok | — | Reducción de boilerplate |
| Springdoc OpenAPI | 2.7.0 | Documentación Swagger UI |
| JUnit 5 + Mockito | — | Framework de testing |

---

## Endpoint

### `GET /api/prices`

Devuelve la tarifa de precio vigente con mayor prioridad para la combinación indicada.

#### Parámetros de consulta

| Parámetro | Tipo | Formato | Obligatorio | Descripción |
|-----------|------|---------|-------------|-------------|
| `applicationDate` | String | `yyyy-MM-dd'T'HH:mm:ss` | Sí | Fecha y hora de consulta |
| `productId` | Long | — | Sí | Identificador del producto |
| `brandId` | Long | — | Sí | Identificador de la marca |

#### Respuesta exitosa — `200 OK`

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14 00:00:00",
  "endDate": "2020-12-31 23:59:59",
  "price": 35.5
}
```

#### Respuesta de error — `404 Not Found`

```json
{
  "timestamp": "2020-06-14T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No price found for brandId=1, productId=35455, date=2020-06-14T10:00:00"
}
```

#### Respuesta de error — `400 Bad Request`

```json
{
  "timestamp": "2020-06-14T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error",
  "details": {
    "applicationDate": "must not be null"
  }
}
```

---

## Ejemplos de uso

```bash
# Tarifa 1 — precio base (10:00 del 14/06/2020)
curl "http://localhost:8081/api/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"

# Tarifa 2 — tarifa promocional con mayor prioridad (16:00 del 14/06/2020)
curl "http://localhost:8081/api/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"

# Tarifa 4 — tarifa vigente a partir del 15/06 por la tarde
curl "http://localhost:8081/api/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

---

## Ejecución

### Requisitos previos

- Java 21 o superior
- Maven 3.8+ (o usar el wrapper incluido `./mvnw`)

### Arrancar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación arranca en `http://localhost:8081` usando el perfil `dev` (activo por defecto via `application.properties`). La configuración específica de entorno reside en `application-dev.properties`.

### Consola H2

Disponible en `http://localhost:8081/h2-console` con los siguientes parámetros:

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:pricesdb` |
| Usuario | `sa` |
| Contraseña | *(vacía)* |

### Documentación interactiva (Swagger UI)

Acceder a `http://localhost:8081/swagger-ui.html` para explorar y probar el endpoint de forma interactiva.

---

## Tests

Se han implementado cinco niveles de testing:

| Clase | Tipo | Descripción |
|-------|------|-------------|
| `DemoApplicationTests` | Humo | Verifica que el contexto de Spring arranca correctamente |
| `PriceServiceTest` | Unitario | Lógica del caso de uso con mock del puerto de repositorio |
| `JpaPriceRepositoryTest` | Integración JPA | Verifica la consulta JPQL, prioridad y resultado vacío |
| `PriceControllerIntegrationTest` | Integración Web | Valida los 5 escenarios requeridos con `@SpringBootTest` + `MockMvc` |
| `GlobalExceptionHandlerTest` | Web (MockMvc) | Comprueba códigos HTTP y cuerpo de las respuestas de error |

### Ejecutar todos los tests

```bash
./mvnw test
```

### Escenarios de prueba requeridos

| # | Fecha/Hora de consulta | Producto | Marca | Tarifa esperada | Precio esperado |
|---|------------------------|----------|-------|-----------------|-----------------|
| 1 | 2020-06-14 10:00:00 | 35455 | 1 | 1 | 35,50 € |
| 2 | 2020-06-14 16:00:00 | 35455 | 1 | 2 | 25,45 € |
| 3 | 2020-06-14 21:00:00 | 35455 | 1 | 1 | 35,50 € |
| 4 | 2020-06-15 10:00:00 | 35455 | 1 | 3 | 30,50 € |
| 5 | 2020-06-16 21:00:00 | 35455 | 1 | 4 | 38,95 € |

---

## Datos de prueba

Al arrancar, `data.sql` inserta automáticamente los siguientes registros en la tabla `PRICES`:

| ID | Marca | Producto | Tarifa | Inicio | Fin | Precio | Moneda | Prioridad |
|----|-------|----------|--------|--------|-----|--------|--------|-----------|
| 1 | 1 | 35455 | 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 35,50 | EUR | 0 |
| 2 | 1 | 35455 | 2 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 25,45 | EUR | 1 |
| 3 | 1 | 35455 | 3 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 30,50 | EUR | 1 |
| 4 | 1 | 35455 | 4 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 38,95 | EUR | 1 |

El esquema de tablas se gestiona mediante `schema.sql` (ejecutado antes que `data.sql` al arrancar), con `spring.jpa.hibernate.ddl-auto=none` para que Hibernate no interfiera.

---

## Mejoras posibles

- **Estructura multi-módulo Maven** para separar las capas en módulos independientes.
- **Base de datos real** (PostgreSQL) para entornos de producción.
- **Métricas y observabilidad** con Micrometer y Actuator.
- **Autenticación y autorización** mediante Spring Security + JWT.
- **Tests de contrato** (Spring Cloud Contract) para garantizar la compatibilidad del API.

---

## Contribuciones

Se agradece cualquier contribución al proyecto. Para contribuir:

1. **Fork del repositorio**: cree una copia del proyecto en su cuenta de GitHub.
2. **Realizar cambios**: haga los cambios deseados en su copia del proyecto.
3. **Crear pull request**: envíe una solicitud de cambios al repositorio original.

---

## Licencia

Este proyecto se encuentra bajo la licencia [MIT](https://opensource.org/licenses/MIT).

---

## Contacto

Para cualquier consulta, abra una issue en el repositorio o contacte por [correo electrónico](mailto:eperezm1986@gmail.com).
