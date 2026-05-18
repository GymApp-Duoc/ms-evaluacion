package com.gymapp.ms_evaluacion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionRequestDTO {

    @NotNull(message = "El ID del miembro es obligatorio")
    private Long miembroId;

    @NotNull(message = "El ID del evaluador es obligatorio")
    private Long evaluadorId;

    @NotNull(message = "La fecha de evaluación es obligatoria")
    @PastOrPresent(message = "La fecha de evaluación no puede ser en el futuro")
    private LocalDate fechaEvaluacion;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "20.0", message = "El peso ingresado no es válido")
    private Double peso;

    private Double porcentajeGrasa;

    private Double masaMuscular;

    private String observaciones;
}

