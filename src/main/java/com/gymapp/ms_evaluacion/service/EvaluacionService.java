package com.gymapp.ms_evaluacion.service;

import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import java.util.List;

public interface EvaluacionService {
    List<EvaluacionResponseDTO> listarTodas();
    EvaluacionResponseDTO obtenerPorId(Long id);
    List<EvaluacionResponseDTO> obtenerHistorialPorMiembro(Long miembroId);
    EvaluacionResponseDTO crear(EvaluacionRequestDTO dto);
    EvaluacionResponseDTO actualizar(Long id, EvaluacionRequestDTO dto);
    void eliminar(Long id);
}

