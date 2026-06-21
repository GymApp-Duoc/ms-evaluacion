package com.gymapp.ms_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "evaluaciones")
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "miembro_id", nullable = false)
    private Long miembroId;

    @Column(name = "evaluador_id", nullable = false)
    private Long evaluadorId;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(nullable = false)
    private Double peso;

    @Column(name = "porcentaje_grasa")
    private Double porcentajeGrasa;

    @Column(name = "masa_muscular")
    private Double masaMuscular;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo;
}

