package com.gymapp.ms_evaluacion.service;

import com.gymapp.ms_evaluacion.client.GamificacionClient;
import com.gymapp.ms_evaluacion.client.MiembroClient;
import com.gymapp.ms_evaluacion.client.NotificacionClient;
import com.gymapp.ms_evaluacion.client.SuscripcionClient;
import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.exception.BusinessException;
import com.gymapp.ms_evaluacion.exception.RecursoNoEncontradoException;
import com.gymapp.ms_evaluacion.model.Evaluacion;
import com.gymapp.ms_evaluacion.repository.EvaluacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository repository;
    private final MiembroClient miembroClient;
    private final SuscripcionClient suscripcionClient;
    private final GamificacionClient gamificacionClient;
    private final NotificacionClient notificacionClient;

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> listarTodas() {
        return repository.findByActivoTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluacionResponseDTO obtenerPorId(Long id) {
        Evaluacion evaluacion = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada."));
        return mapearADto(evaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> obtenerHistorialPorMiembro(Long miembroId) {
        return repository.findByMiembroIdAndActivoTrueOrderByFechaEvaluacionDesc(miembroId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvaluacionResponseDTO crear(EvaluacionRequestDTO dto) {
        log.info("[EVALUACION] Iniciando proceso de evaluación para miembro ID: {}", dto.getMiembroId());

        try {
            miembroClient.obtenerPorId(dto.getMiembroId());
        } catch (FeignException.NotFound e) {
            throw new BusinessException("El miembro con ID " + dto.getMiembroId() + " no existe.");
        }


        try {
            var respuesta = suscripcionClient.obtenerSuscripcionesPorMiembro(dto.getMiembroId());
            boolean listaVacia = true;


            if (respuesta instanceof java.util.List) {
                listaVacia = ((java.util.List<?>) respuesta).isEmpty();
            } else if (respuesta instanceof org.springframework.http.ResponseEntity) {
                Object body = ((org.springframework.http.ResponseEntity<?>) respuesta).getBody();
                if (body instanceof java.util.List) {
                    listaVacia = ((java.util.List<?>) body).isEmpty();
                }
            }

            if (listaVacia) {
                throw new BusinessException("El miembro no tiene una suscripción activa. No se puede realizar la evaluación.");
            }
        } catch (FeignException e) {
            log.error("Fallo al comunicarse con ms-suscripciones", e);
            throw new BusinessException("Servicio de validación de suscripciones no disponible o cliente no encontrado.");
        }

        Evaluacion evaluacion = new Evaluacion(
                null,
                dto.getMiembroId(),
                dto.getEvaluadorId(),
                dto.getFechaEvaluacion(),
                dto.getPeso(),
                dto.getPorcentajeGrasa(),
                dto.getMasaMuscular(),
                dto.getObservaciones(),
                true
        );

        Evaluacion guardada = repository.save(evaluacion);
        log.info("[EVALUACION] Evaluación registrada exitosamente con ID: {}", guardada.getId());

        emitirEventosIntegracion(guardada);
        return mapearADto(guardada);
    }

    @Override
    @Transactional
    public EvaluacionResponseDTO actualizar(Long id, EvaluacionRequestDTO dto) {
        Evaluacion existente = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada."));

        if (!existente.getMiembroId().equals(dto.getMiembroId())) {
            throw new BusinessException("Inconsistencia: No se puede cambiar el miembro de una evaluación ya realizada.");
        }

        existente.setPeso(dto.getPeso());
        existente.setPorcentajeGrasa(dto.getPorcentajeGrasa());
        existente.setMasaMuscular(dto.getMasaMuscular());
        existente.setObservaciones(dto.getObservaciones());


        return mapearADto(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Evaluacion evaluacion = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada."));
        evaluacion.setActivo(false);
        repository.save(evaluacion);
        log.info("[EVALUACION] Evaluación ID {} eliminada lógicamente.", id);
    }

    private void emitirEventosIntegracion(Evaluacion evaluacion) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("miembroId", evaluacion.getMiembroId());
            evento.put("accion", "EVALUACION_FISICA_COMPLETADA");
            evento.put("puntosBase", 30);
            gamificacionClient.enviarEvento(evento);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Ms-Gamificación no disponible: {}", e.getMessage());
        }

        try {
            Map<String, Object> noti = new HashMap<>();
            noti.put("miembroId", evaluacion.getMiembroId());
            noti.put("titulo", "Nuevos Resultados Disponibles");
            noti.put("mensaje", "Tu evaluación física ha sido registrada. Peso actual: " + evaluacion.getPeso() + "kg. Revisa tu progreso en la app.");
            notificacionClient.enviarNotificacion(noti);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Ms-Notificaciones no disponible: {}", e.getMessage());
        }
    }

    private EvaluacionResponseDTO mapearADto(Evaluacion entity) {
        return EvaluacionResponseDTO.builder()
                .id(entity.getId())
                .miembroId(entity.getMiembroId())
                .evaluadorId(entity.getEvaluadorId())
                .fechaEvaluacion(entity.getFechaEvaluacion())
                .peso(entity.getPeso())
                .porcentajeGrasa(entity.getPorcentajeGrasa())
                .masaMuscular(entity.getMasaMuscular())
                .observaciones(entity.getObservaciones())
                .build();
    }
}