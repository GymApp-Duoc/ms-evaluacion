package com.gymapp.ms_evaluacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionResponseDTO {
    private Long id;
    private Long miembroId;
    private Long evaluadorId;
    private LocalDate fechaEvaluacion;
    private Double peso;
    private Double porcentajeGrasa;
    private Double masaMuscular;
    private String observaciones;
}

