# Registro de prompts principales

Durante el desarrollo del microservicio Dinosaur se utilizó un asistente de IA en **Cursor** (chat y agente). Este archivo recoge los **prompts representativos** y el desenlace práctico (respuesta resumida y acción tomada).

**Práctica habitual:** antes de integrar cambios, se usó la función de **Code Review** de Cursor contra el branch remoto para detectar imports sin usar, casos límite y posibles regresiones.

---

## 1. Spring y arquitectura hexagonal (beans sin `@Service` en aplicación)

**Prompt (idea):**

> ¿Cómo puedo invocar el caso de uso sin poner anotaciones del framework como `@Service` en la capa de aplicación?

**Respuesta (resumen):** En hexagonal, los beans de Spring suelen declararse en **infraestructura** (p. ej. una `@Configuration`), instanciando la implementación pura del caso de uso y exponiendo el puerto de entrada.

**Acción:** Se añadió configuración en infraestructura para registrar los beans necesarios sin acoplar la capa de aplicación a Spring.

---

## 2. Error al iniciar tras configurar RabbitMQ (scheduler / contexto)

**Prompt (idea):**

> Tras configurar RabbitMQ, al arrancar aparece `UnsatisfiedDependencyException` relacionado con el bean `dinosaurStatusScheduler` / inicialización del contexto.

**Respuesta:** En ese intercambio el agente entró en bucle y se **canceló** la conversación.

**Acción:** Se verificó la **compatibilidad de versiones** (p. ej. Java 25 vs Spring Boot 4 frente a un stack estable); se alineó el proyecto con **Java 21** y **Spring Boot 3.x** para un arranque coherente.

---

## 3. Tests del controlador: `LocalDateTime` y Jackson

**Prompt (idea):**

> Los tests fallan con `InvalidDefinitionException`: `LocalDateTime` no soportado por defecto (JSR-310 / `jackson-datatype-jsr310`).

**Respuesta (resumen):** Registrar `JavaTimeModule` en el `ObjectMapper` usado en el test (o equivalente en el setup de MVC).

**Acción:** Se configuró el `ObjectMapper` en los tests con el módulo de fechas necesario para serializar/deserializar `discoveryDate` y similares.

---

## 4. Cobertura del workflow en tests de integración

**Prompt (idea):**

> ¿Podrías verificar si el test de integración cubre todo el workflow del dinosaurio?

**Respuesta (resumen):** El escenario inicial cubría sobre todo el **happy path** del `POST`; el flujo real (otros endpoints, estados, mensajería) quedaba incompleto.

**Acción:** Con el **modo agente** se ampliaron escenarios en `DinosaurControllerIntegrationTests` (u homólogos) para acercar la suite al comportamiento deseado.

---

## 5. Verificar envío real a RabbitMQ en tests

**Prompt (idea):**

> ¿Qué añadiría en los tests de cambio de estado para comprobar que el mensaje se publicó en RabbitMQ?

**Respuesta (resumen):** Inyectar `RabbitTemplate` (o API de test equivalente) y **consumir** de la cola asociada al contenedor de Testcontainers para asertar cuerpo y propiedades del mensaje.

**Acción:** Se pidió al agente incorporar lectura de la cola en los métodos pertinentes y aserciones sobre el mensaje enviado.

---

## 6. Aislamiento entre tests: base de datos y cola

**Prompt (idea):**

> ¿Se puede borrar la base de datos y vaciar los mensajes de la cola en un `@BeforeEach`?

**Respuesta (resumen):** Sí; sin limpiar estado entre tests aparecen **fallos intermitentes** por datos o mensajes residuales.

**Acción:** En `@BeforeEach`, limpieza de repositorios / `deleteAll` según el caso y **purge** de la cola de RabbitMQ usada en el test.

---

## 7. README: legibilidad y operación del proyecto

**Prompt (idea):**

> Mejorar el README: más legible, arquitectura clara, cómo levantar, cómo testear y qué componentes tiene el sistema.

**Acción:** El agente reestructuró el README con secciones claras, tablas, diagramas (p. ej. Mermaid) y pasos de Docker / Maven.

---

## 8. Contenedorización (Dockerfile y Docker Compose)

**Prompt (idea, típico en esta línea de trabajo):**

> Añadir un `Dockerfile` multi-stage (build Maven + runtime JRE), `.dockerignore` y un servicio `app` en `docker-compose` que hable con Postgres y RabbitMQ por **nombre de servicio**, no por `localhost`.

**Acción:** Imagen de la aplicación en Compose junto a Postgres y RabbitMQ; variables `SPRING_DATASOURCE_*` y `SPRING_RABBITMQ_*` apuntando a `postgres` y `rabbitmq`; `depends_on` con condición de salud cuando aplica.

---

## Notas

- Los textos entre comillas son **parafrases** del intent del prompt; la redacción exacta puede haber variado entre sesiones.
- Cuando la respuesta automática no aportó, la resolución vino de **documentación oficial**, pruebas locales y criterio del desarrollador.
