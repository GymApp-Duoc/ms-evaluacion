package com.gymapp.ms_evaluacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos requeridos para registrar o actualizar una evaluación física")
public class EvaluacionRequestDTO {

    @NotNull(message = "El ID del miembro es obligatorio")
    @Schema(description = "ID del miembro evaluado", example = "1")
    private Long miembroId;

    @NotNull(message = "El ID del evaluador es obligatorio")
    @Schema(description = "ID del entrenador/kinesiólogo que realiza la medición", example = "3")
    private Long evaluadorId;

    @NotNull(message = "La fecha de evaluación es obligatoria")
    @PastOrPresent(message = "La fecha de evaluación no puede ser en el futuro")
    @Schema(description = "Fecha exacta de la toma de medidas", example = "2026-06-25")
    private LocalDate fechaEvaluacion;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "20.0", message = "El peso ingresado no es válido")
    @Schema(description = "Peso en kilogramos", example = "75.5")
    private Double peso;

    @Schema(description = "Porcentaje de grasa corporal", example = "18.2")
    private Double porcentajeGrasa;

    @Schema(description = "Masa muscular en kilogramos", example = "35.4")
    private Double masaMuscular;

    @Schema(description = "Anotaciones clínicas o recomendaciones", example = "Paciente presenta leve asimetría en tren inferior.")
    private String observaciones;
}