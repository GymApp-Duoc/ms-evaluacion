package com.gymapp.ms_evaluacion.assembler;

import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.model.Evaluacion;
import org.springframework.stereotype.Component;

@Component
public class EvaluacionAssembler {

    public EvaluacionResponseDTO toResponseDTO(Evaluacion evaluacion) {
        if (evaluacion == null) return null;
        return EvaluacionResponseDTO.builder()
                .id(evaluacion.getId())
                .miembroId(evaluacion.getMiembroId())
                .evaluadorId(evaluacion.getEvaluadorId())
                .fechaEvaluacion(evaluacion.getFechaEvaluacion())
                .peso(evaluacion.getPeso())
                .porcentajeGrasa(evaluacion.getPorcentajeGrasa())
                .masaMuscular(evaluacion.getMasaMuscular())
                .observaciones(evaluacion.getObservaciones())
                .build();
    }

    public Evaluacion toEntity(EvaluacionRequestDTO dto) {
        if (dto == null) return null;
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setMiembroId(dto.getMiembroId());
        evaluacion.setEvaluadorId(dto.getEvaluadorId());
        evaluacion.setFechaEvaluacion(dto.getFechaEvaluacion());
        evaluacion.setPeso(dto.getPeso());
        evaluacion.setPorcentajeGrasa(dto.getPorcentajeGrasa());
        evaluacion.setMasaMuscular(dto.getMasaMuscular());
        evaluacion.setObservaciones(dto.getObservaciones());
        evaluacion.setActivo(true);
        return evaluacion;
    }
}