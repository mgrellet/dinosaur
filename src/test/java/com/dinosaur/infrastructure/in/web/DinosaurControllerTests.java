package com.dinosaur.infrastructure.in.web;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.port.in.DeleteDinosaurUseCase;
import com.dinosaur.application.port.in.GetAllDinosaursUseCase;
import com.dinosaur.application.port.in.GetDinosaurByIdUseCase;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.mapper.InfrastructureMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DinosaurControllerTests {

        private MockMvc mockMvc;

        @Mock
        private CreateDinosaurUseCase createUseCase;
        @Mock
        private GetDinosaurByIdUseCase getByIdUseCase;
        @Mock
        private GetAllDinosaursUseCase getAllUseCase;
        @Mock
        private UpdateDinosaurUseCase updateUseCase;
        @Mock
        private DeleteDinosaurUseCase deleteUseCase;

        @InjectMocks
        private DinosaurController controller;

        ObjectMapper objectMapper;

        LocalDateTime now;

        @BeforeEach
        void setUp() {
                objectMapper = new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                                .build();

                now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        }

        @Test
        void testCreateDinosaur() throws Exception {

                DinosaurRequest request = new DinosaurRequest("T-Rex", "Dinosaur", now, now, "Alive");
                DinosaurResult dinosaurResult = new DinosaurResult(1L, "T-Rex", "Dinosaur", now, now, "Alive");

                when(createUseCase.execute(InfrastructureMapper.toCommand(request)))
                                .thenReturn(dinosaurResult);
                mockMvc.perform(post("/dinosaur")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.name").value("T-Rex"))
                                .andExpect(jsonPath("$.species").value("Dinosaur"))
                                .andExpect(jsonPath("$.discoveryDate").value(now.toString()))
                                .andExpect(jsonPath("$.extinctionDate").value(now.toString()))
                                .andExpect(jsonPath("$.status").value("Alive"))
                                .andExpect(status().isCreated());
        }

        @Test
        void testCreateDinosaurWithInvalidRequest() throws Exception {
                DinosaurRequest request = new DinosaurRequest("", "Dinosaur", now, now, "Alive");
                mockMvc.perform(post("/dinosaur")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testGetAllDinosaurs() throws Exception {
                List<DinosaurResult> dinosaurs = Arrays.asList(
                                new DinosaurResult(1L, "T-Rex", "Dinosaur", now, now, "Alive"),
                                new DinosaurResult(2L, "Velociraptor", "Dinosaur", now, now, "Alive"));

                when(getAllUseCase.execute()).thenReturn(dinosaurs);

                mockMvc.perform(get("/dinosaur"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].id").value(1L))
                                .andExpect(jsonPath("$[0].name").value("T-Rex"))
                                .andExpect(jsonPath("$[0].species").value("Dinosaur"))
                                .andExpect(jsonPath("$[0].discoveryDate").value(now.toString()))
                                .andExpect(jsonPath("$[0].extinctionDate").value(now.toString()))
                                .andExpect(jsonPath("$[0].status").value("Alive"))
                                .andExpect(jsonPath("$[1].id").value(2L))
                                .andExpect(jsonPath("$[1].name").value("Velociraptor"))
                                .andExpect(jsonPath("$[1].species").value("Dinosaur"))
                                .andExpect(jsonPath("$[1].discoveryDate").value(now.toString()))
                                .andExpect(jsonPath("$[1].extinctionDate").value(now.toString()))
                                .andExpect(jsonPath("$[1].status").value("Alive"));
        }

        @Test
        void testGetDinosaurById() throws Exception {
                DinosaurResult dinosaur = new DinosaurResult(1L, "T-Rex", "Dinosaur", now, now, "Alive");

                when(getByIdUseCase.execute(dinosaur.id())).thenReturn(dinosaur);

                mockMvc.perform(get("/dinosaur/" + dinosaur.id()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(dinosaur.id()))
                                .andExpect(jsonPath("$.name").value(dinosaur.name()))
                                .andExpect(jsonPath("$.species").value(dinosaur.species()))
                                .andExpect(jsonPath("$.discoveryDate").value(dinosaur.discoveryDate().toString()))
                                .andExpect(jsonPath("$.extinctionDate").value(dinosaur.extinctionDate().toString()))
                                .andExpect(jsonPath("$.status").value(dinosaur.status()));
        }

        @Test
        void testUpdateDinosaur() throws Exception {
                DinosaurRequest request = new DinosaurRequest("T-Rex", "Dinosaur", now, now, "Alive");

                DinosaurResult dinosaurResult = new DinosaurResult(1L, "T-Rex", "Dinosaur", now, now, "Alive");

                when(updateUseCase.execute(dinosaurResult.id(), InfrastructureMapper.toCommand(request)))
                                .thenReturn(dinosaurResult);
                mockMvc.perform(put("/dinosaur/" + dinosaurResult.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(dinosaurResult.id()))
                                .andExpect(jsonPath("$.name").value(dinosaurResult.name()))
                                .andExpect(jsonPath("$.species").value(dinosaurResult.species()))
                                .andExpect(jsonPath("$.discoveryDate").value(dinosaurResult.discoveryDate().toString()))
                                .andExpect(jsonPath("$.extinctionDate")
                                                .value(dinosaurResult.extinctionDate().toString()))
                                .andExpect(jsonPath("$.status").value(dinosaurResult.status()));
        }

        @Test
        void testDeleteDinosaur() throws Exception {
                Long id = 1L;
                mockMvc.perform(delete("/dinosaur/" + id)).andExpect(status().isNoContent());
                verify(deleteUseCase).execute(id);
        }
}
