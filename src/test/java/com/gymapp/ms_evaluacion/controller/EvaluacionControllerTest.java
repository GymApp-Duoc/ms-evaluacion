package com.gymapp.ms_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.service.EvaluacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionControllerTest {

    private MockMvc mockMvc;

    @Mock private EvaluacionService evaluacionService;
    @InjectMocks private EvaluacionController evaluacionController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(evaluacionController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void crear_PeticionValida_Retorna201() throws Exception {

        EvaluacionRequestDTO request = new EvaluacionRequestDTO(1L, 2L, LocalDate.now(), 80.0, 20.0, 30.0, "Detalle");
        EvaluacionResponseDTO response = EvaluacionResponseDTO.builder().id(10L).peso(80.0).build();

        when(evaluacionService.crear(any(EvaluacionRequestDTO.class))).thenReturn(response);


        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.peso").value(80.0));
    }

    @Test
    void crear_PesoInvalido_Retorna400() throws Exception {

        EvaluacionRequestDTO request = new EvaluacionRequestDTO(1L, 2L, LocalDate.now(), 10.0, 20.0, 30.0, "Detalle");


        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}