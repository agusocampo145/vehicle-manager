# Vehicle Manager API – Challenge Técnico

API REST desarrollada en **Java + Spring Boot** para la gestión de vehículos y sus mantenimientos, como parte de un ejercicio técnico.

El objetivo fue construir una solución **clara, mantenible y extensible**, priorizando buenas prácticas de diseño, separación de responsabilidades y legibilidad del código.

---

## 🧱 Stack Tecnológico

- Java 17  
- Spring Boot (Web, JPA, Validation)  
- Hibernate  
- PostgreSQL  
- Flyway (migraciones)  
- Gradle  
- Docker / Docker Compose  
- Swagger / OpenAPI (springdoc)  

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos

- Java 17  
- Docker + Docker Compose  

### Pasos

**Levantar la base de datos:**
```bash
docker compose up -d
```

**Ejecutar la aplicación:**
```bash
./gradlew bootRun
```

La API quedará disponible en:
```
http://localhost:8080
```

Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📌 Funcionalidades principales

### Vehículos
- Registrar vehículo (patente única)
- Consultar vehículo por ID o patente
- Actualizar kilometraje
- Consultar disponibilidad del vehículo

> Un vehículo se considera **no disponible** si tiene al menos un mantenimiento en estado `PENDIENTE` o `EN_PROCESO`.

### Mantenimientos
- Registrar mantenimiento asociado a un vehículo
- Listar historial de mantenimientos por vehículo
- Listar mantenimientos activos
- Cambiar estado (`PENDIENTE → EN_PROCESO / CANCELADO`)
- Completar mantenimiento con costo final
- Calcular costo total de mantenimientos completados

---

## 🧠 Decisiones de diseño y buenas prácticas

### 1️⃣ Separación clara por capas

- **Controllers**: exponen endpoints y transforman DTOs  
- **Services**: contienen la lógica de negocio  
- **Domain (Entidades)**: encapsulan reglas e invariantes  
- **Repositories**: acceso a datos  

Se evitó mezclar lógica de negocio con HTTP, logging o validaciones de infraestructura.

---

### 2️⃣ DTOs por caso de uso

Cada endpoint utiliza un DTO específico en lugar de reutilizar una entidad o un DTO genérico.

Esto permite:
- Contratos de API claros
- Validaciones precisas
- Evitar actualizaciones accidentales de campos
- Mejor documentación en Swagger

---

### 3️⃣ Manejo centralizado de errores

Se utiliza un `@RestControllerAdvice` para:
- Centralizar el manejo de excepciones
- Mantener los services libres de `try/catch` innecesarios
- Devolver respuestas de error estructuradas y documentables

Ejemplo de respuesta de error:
```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "El costo final debe ser cero o positivo",
  "timestamp": "2026-02-03T01:23:45"
}
```

Se optó por un DTO de error propio en lugar de usar `ErrorResponse` / `ProblemDetail` de Spring para:
- Evitar acoplar el contrato de la API al framework
- Mantener estabilidad y claridad del response

---

### 4️⃣ Transiciones de estado explícitas

Los estados de `Mantenimiento` no se cambian arbitrariamente.

En lugar de setters genéricos, se modelaron **acciones de dominio**:
- `iniciar()`
- `cancelar()`
- `completar(costoFinal)`

Esto evita estados inválidos y hace las reglas explícitas.

---

### 5️⃣ Uso de `record` para DTOs

Los DTOs se implementaron como `record` (Java 14+) para:
- Reducir boilerplate
- Garantizar inmutabilidad
- Representar claramente contratos de entrada/salida

Las entidades JPA continúan siendo clases tradicionales.

---

### 6️⃣ Inyección de dependencias con Spring

En este proyecto se utilizó inyección de dependencias mediante `@Autowired`, aprovechando las capacidades nativas que ofrece Spring Boot para la gestión del ciclo de vida de los beans.

Si bien la inyección por constructor es una práctica común y recomendada en muchos contextos, en este caso se optó por `@Autowired` para:
- Mantener el código más directo y legible
- Aprovechar el soporte completo del framework
- Evitar sobrecargar constructores en clases de servicio simples

Spring garantiza la correcta inicialización de dependencias y el uso de `@Autowired` resulta adecuado y seguro dentro del contexto del framework, sin comprometer la mantenibilidad ni la testabilidad del código.

---

### 7️⃣ Swagger / OpenAPI

Los endpoints están documentados con anotaciones OpenAPI (`@Operation`, `@ApiResponses`, etc.), permitiendo:
- Exploración interactiva
- Contratos claros
- Facilitar pruebas con Postman o frontend

---

## 🧪 Testing

El foco del ejercicio estuvo puesto en el diseño y la arquitectura.  
La estructura del proyecto permite agregar tests unitarios fácilmente en la capa de service utilizando mocks de repositorios.

---

## 🤖 Uso de IA como asistencia

Durante el desarrollo se utilizó **ChatGPT 5.2** como apoyo puntual para:
- Generar código boilerplate
- Validar decisiones de diseño
- Contrastar buenas prácticas de arquitectura

Todas las decisiones finales de diseño, estructura y comportamiento de la aplicación fueron analizadas, adaptadas e implementadas conscientemente, priorizando claridad y mantenibilidad.

---

## 📎 Consideraciones finales

- Se permitió que un vehículo tenga múltiples mantenimientos activos, ya que el enunciado no lo prohíbe.
- La disponibilidad del vehículo se calcula por la existencia de **al menos un mantenimiento activo**, alineado al requerimiento.
- El proyecto está pensado como una base clara y extensible, no como un sistema cerrado.
-  Esta versión está pensada para uso interno. Se asume que los consumidores cuentan con acceso a la documentación (Swagger/OpenAPI) y conocen los valores válidos (por ejemplo, enums).
