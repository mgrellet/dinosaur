package com.dinosaur.infrastructure.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.dinosaur.application.port.in.UpdateScheduledDinosaurStatusesUseCase;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.dto.DinosaurResponse;
import com.dinosaur.infrastructure.out.messaging.DinosaurStatusUpdateMessage;
import com.dinosaur.infrastructure.out.persistance.DinosaurJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class DinosaurControllerIntegrationTests {

    private static final long RABBIT_RECEIVE_TIMEOUT_MS = 5000L;
    private static final long RABBIT_NO_MESSAGE_TIMEOUT_MS = 500L;


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("dinosaur")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UpdateScheduledDinosaurStatusesUseCase updateScheduledDinosaurStatusesUseCase;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private DinosaurJpaRepository dinosaurJpaRepository;

    @Value("${rabbitmq.queue}")
    private String queueName;

    @BeforeEach
    void resetIntegrationState() {
        rabbitAdmin.purgeQueue(queueName);
        dinosaurJpaRepository.deleteAll();
    }

    private static final LocalDateTime PAST_DISCOVERY = LocalDateTime.parse("1902-01-01T00:00:00");
    private static final LocalDateTime FUTURE_EXTINCTION = LocalDateTime.parse("2099-12-31T23:59:59");

    // --- CREATE ---

    @Test
    void testCreateDinosaur() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "Triceratops", "Ceratopsid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());

        mockMvc.perform(post("/dinosaur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Triceratops"))
                .andExpect(jsonPath("$.status").value(DinosaurStatus.ALIVE.name()));
    }

    @Test
    void testCreateDinosaur_duplicateName_returns400() throws Exception {
        DinosaurRequest first = new DinosaurRequest(
                "Diplodocus", "Diplodocid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        createDinosaurAndGetId(first);

        DinosaurRequest duplicate = new DinosaurRequest(
                "Diplodocus", "Diplodocid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());

        mockMvc.perform(post("/dinosaur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDinosaur_invalidDates_returns400() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "BadDatesDino", "Ceratopsid", FUTURE_EXTINCTION, PAST_DISCOVERY, DinosaurStatus.ALIVE.name());

        mockMvc.perform(post("/dinosaur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDinosaur_missingRequiredFields_returns400() throws Exception {
        DinosaurRequest request = new DinosaurRequest(null, null, PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());

        mockMvc.perform(post("/dinosaur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // --- GET ---

    @Test
    void testGetAllDinosaurs() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "Brachiosaurus", "Brachiosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        createDinosaurAndGetId(request);

        mockMvc.perform(get("/dinosaur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));    }

    @Test
    void testGetDinosaurById() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "Stegosaurus", "Stegosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(request);

        mockMvc.perform(get("/dinosaur/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Stegosaurus"));
    }

    @Test
    void testGetDinosaurById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/dinosaur/99999"))
                .andExpect(status().isNotFound());
    }

    // --- UPDATE ---

    @Test
    void testUpdateDinosaur() throws Exception {
        DinosaurRequest createRequest = new DinosaurRequest(
                "Velociraptor", "Dromaeosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(createRequest);

        DinosaurRequest updateRequest = new DinosaurRequest(
                "Velociraptor Updated", "Dromaeosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ENDANGERED.name());

        mockMvc.perform(put("/dinosaur/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Velociraptor Updated"))
                .andExpect(jsonPath("$.status").value(DinosaurStatus.ENDANGERED.name()));
    }

    @Test
    void testUpdateDinosaur_invalidStatus_returns400() throws Exception {
        DinosaurRequest createRequest = new DinosaurRequest(
                "Spinosaurus", "Spinosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(createRequest);

        DinosaurRequest updateRequest = new DinosaurRequest(
                "Spinosaurus", "Spinosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, "INVALID_STATUS");

        mockMvc.perform(put("/dinosaur/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate_afterSchedulerMarksAsExtinct_returns400() throws Exception {
        DinosaurRequest createRequest = new DinosaurRequest(
                "ExtinctDino", "Ceratopsid",
                PAST_DISCOVERY,
                LocalDateTime.now().minusHours(1),
                DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(createRequest);

        updateScheduledDinosaurStatusesUseCase.execute();

        DinosaurRequest updateRequest = new DinosaurRequest(
                "ExtinctDino", "Ceratopsid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());

        mockMvc.perform(put("/dinosaur/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE ---

    @Test
    void testDeleteDinosaur() throws Exception {
        DinosaurRequest createRequest = new DinosaurRequest(
                "Ankylosaurus", "Ankylosaurid", PAST_DISCOVERY, FUTURE_EXTINCTION, DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(createRequest);

        mockMvc.perform(delete("/dinosaur/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/dinosaur/" + id))
                .andExpect(status().isNotFound());
    }

    // --- SCHEDULER ---

    @Test
    void testScheduler_transitionsAliveToExtinct() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "SchedulerExtinctDino", "Ceratopsid",
                PAST_DISCOVERY,
                LocalDateTime.now().minusHours(1),
                DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(request);

        updateScheduledDinosaurStatusesUseCase.execute();

        mockMvc.perform(get("/dinosaur/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DinosaurStatus.EXTINCT.name()));

        assertThat((DinosaurStatusUpdateMessage) rabbitTemplate
                .receiveAndConvert(queueName, RABBIT_RECEIVE_TIMEOUT_MS))
                .isNotNull()
                .satisfies(message -> {
                    assertThat(message.dinosaurId()).isEqualTo(id);
                    assertThat(message.newStatus()).isEqualTo(DinosaurStatus.EXTINCT.name());
                    assertThat(message.timestamp()).isNotNull();
                });
    }

    @Test
    void testScheduler_transitionsAliveToEndangered() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "EndangeredDino", "Ceratopsid",
                PAST_DISCOVERY,
                LocalDateTime.now().plusHours(12),
                DinosaurStatus.ALIVE.name());
        Long id = createDinosaurAndGetId(request);

        updateScheduledDinosaurStatusesUseCase.execute();

        mockMvc.perform(get("/dinosaur/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DinosaurStatus.ENDANGERED.name()));

        assertThat((DinosaurStatusUpdateMessage) rabbitTemplate
                .receiveAndConvert(queueName, RABBIT_RECEIVE_TIMEOUT_MS))
                .isNotNull()
                .satisfies(message -> {
                    assertThat(message.dinosaurId()).isEqualTo(id);
                    assertThat(message.newStatus()).isEqualTo(DinosaurStatus.ENDANGERED.name());
                    assertThat(message.timestamp()).isNotNull();
                });
    }

    @Test
    void testScheduler_noStatusChange_doesNotPublishMessage() throws Exception {
        DinosaurRequest request = new DinosaurRequest(
                "StableDino", "Ceratopsid",
                PAST_DISCOVERY,
                LocalDateTime.now().plusDays(7),
                DinosaurStatus.ALIVE.name());
        createDinosaurAndGetId(request);

        updateScheduledDinosaurStatusesUseCase.execute();

        Object message = rabbitTemplate.receiveAndConvert(queueName, RABBIT_NO_MESSAGE_TIMEOUT_MS);
        assertThat(message).isNull();
    }

    private Long createDinosaurAndGetId(DinosaurRequest request) throws Exception {
        String response = mockMvc.perform(post("/dinosaur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, DinosaurResponse.class).getId();
    }

}
