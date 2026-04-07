# Reporte de uso de inteligencia artificial

Este documento describe **cómo se empleó Cursor y la IA** durante el desarrollo del microservicio Dinosaur (Spring Boot, arquitectura hexagonal, PostgreSQL y RabbitMQ). El objetivo es dejar constancia transparente del rol del asistente frente a decisiones y validación humana.

---

## Resumen

La IA actuó como **copiloto de programación**: aceleró redacción de código y configuración, sugirió patrones alineados con hexagonal y Spring, y ayudó a ampliar tests. **No sustituyó** la revisión manual, la ejecución de tests ni las decisiones de diseño; esas quedaron a cargo del desarrollador.

---

## Herramientas de Cursor y cómo se usaron

| Modo / función | Uso en este proyecto |
|------------------|----------------------|
| **Cursor Tab (autocompletado)** | Completar métodos repetitivos, firmas, imports y fragmentos pequeños manteniendo el estilo del código existente. |
| **Chat / preguntas puntuales** | Dudas de configuración Spring, errores de arranque, serialización Jackson en tests y convenciones hexagonales (por ejemplo, dónde declarar beans sin “ensuciar” la capa de aplicación). |
| **Agente (Composer)** | Tareas más amplias: ampliar tests de integración, añadir verificación de mensajes en RabbitMQ, limpieza de estado en `@BeforeEach`, y mejoras al README con diagramas y tablas. |
| **Code Review de Cursor** | Antes de commits, revisión de diff frente al branch remoto: imports sin usar, edge cases y posibles regresiones. |

En conjunto, **Tab** aportó velocidad en el teclado; **chat** resolvió bloqueos puntuales con contexto; el **agente** automatizó cambios multiarchivo bajo instrucciones explícitas; la **revisión integrada** sirvió como checklist adicional antes de integrar.

---

## Áreas del desarrollo donde intervino la IA

1. **Arquitectura y Spring:** orientación para enlazar casos de uso como beans desde infraestructura (`@Configuration`), sin anotar la capa de aplicación con `@Service`.
2. **Mensajería y arranque:** análisis de errores de contexto relacionados con RabbitMQ y scheduler; la resolución final incluyó alinear versiones de **Java 21** y **Spring Boot 3.x** por compatibilidad.
3. **Tests:** configuración de `ObjectMapper` con `JavaTimeModule` para `LocalDateTime`; ampliación del workflow en tests de integración; consumo de cola en tests para asertar publicación AMQP; aislamiento entre tests (purge de cola y limpieza de datos).
4. **Contenedores y documentación:** apoyo en `Dockerfile`, `.dockerignore`, servicio `app` en `docker-compose.yml` (variables hacia `postgres` / `rabbitmq`) y redacción estructurada del README (arquitectura, cómo levantar y testear).

---

## ¿Qué sugerencias se descartaron?

- Propuestas que **no encajaban** con estándares de código limpio o con el estilo ya adoptado en el repo.
- Recomendaciones de **librerías externas** para problemas resolubles con el propio ecosistema Spring / estándar de Java.

---

## ¿Cómo se validó el código generado?

1. **Revisión de código:** cada cambio sugerido se contrastó con el diseño hexagonal y con las convenciones del proyecto antes de aceptarlo.
2. **Tests unitarios (JUnit 5, Mockito):** dominio y reglas de negocio (fechas inválidas, dinosaurios extintos, etc.).
3. **Tests de integración (Testcontainers):** PostgreSQL y RabbitMQ reales en contenedor; persistencia y mensajería verificadas de extremo a extremo.
4. **Comprobación arquitectónica:** separación entre dominio, aplicación e infraestructura; entidades JPA y DTOs REST mapeados al modelo de dominio sin fugas hacia adentro.

---

## Limitaciones observadas

En al menos un caso (error de arranque con RabbitMQ / scheduler), el agente entró en un **bucle poco útil** y la consulta se canceló; el problema se resolvió investigando versiones y compatibilidad. Esto refuerza que la IA es una herramienta que **requiere supervisión** y criterio técnico.
