package com.gymapp.ms_evaluacion.service;

import com.gymapp.ms_evaluacion.assembler.EvaluacionAssembler;
import com.gymapp.ms_evaluacion.client.GamificacionClient;
import com.gymapp.ms_evaluacion.client.MiembroClient;
import com.gymapp.ms_evaluacion.client.NotificacionClient;
import com.gymapp.ms_evaluacion.client.SuscripcionClient;
import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.exception.BusinessException;
import com.gymapp.ms_evaluacion.model.Evaluacion;
import com.gymapp.ms_evaluacion.repository.EvaluacionRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceImplTest {

    @Mock private EvaluacionRepository repository;
    @Mock private MiembroClient miembroClient;
    @Mock private SuscripcionClient suscripcionClient;
    @Mock private GamificacionClient gamificacionClient;
    @Mock private NotificacionClient notificacionClient;
    @Mock private EvaluacionAssembler assembler;

    @InjectMocks private EvaluacionServiceImpl evaluacionService;

    @Test
    void crear_MiembroValidoConSuscripcion_RetornaEvaluacion() {

        EvaluacionRequestDTO request = new EvaluacionRequestDTO(1L, 2L, LocalDate.now(), 75.0, 15.0, 35.0, "Ok");
        Evaluacion entity = new Evaluacion(null, 1L, 2L, LocalDate.now(), 75.0, 15.0, 35.0, "Ok", true);
        Evaluacion guardada = new Evaluacion(1L, 1L, 2L, LocalDate.now(), 75.0, 15.0, 35.0, "Ok", true);
        EvaluacionResponseDTO response = EvaluacionResponseDTO.builder().id(1L).peso(75.0).build();


        when(miembroClient.obtenerPorId(1L)).thenReturn(new Object());

        when(suscripcionClient.obtenerSuscripcionesPorMiembro(1L)).thenReturn(List.of(new Object()));
        when(assembler.toEntity(request)).thenReturn(entity);
        when(repository.save(any(Evaluacion.class))).thenReturn(guardada);
        when(assembler.toResponseDTO(guardada)).thenReturn(response);


        EvaluacionResponseDTO resultado = evaluacionService.crear(request);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(75.0, resultado.getPeso());
        verify(gamificacionClient, times(1)).enviarEvento(anyMap());
        verify(notificacionClient, times(1)).enviarNotificacion(anyMap());
    }

    @Test
    void crear_MiembroInvalido_LanzaBusinessException() {

        EvaluacionRequestDTO request = new EvaluacionRequestDTO(99L, 2L, LocalDate.now(), 75.0, 15.0, 35.0, "");
        FeignException.NotFound notFoundException = mock(FeignException.NotFound.class);
        when(miembroClient.obtenerPorId(99L)).thenThrow(notFoundException);


        BusinessException exception = assertThrows(BusinessException.class, () -> evaluacionService.crear(request));
        assertEquals("El miembro con ID 99 no existe.", exception.getMessage());
        verify(repository, never()).save(any());
    }
}