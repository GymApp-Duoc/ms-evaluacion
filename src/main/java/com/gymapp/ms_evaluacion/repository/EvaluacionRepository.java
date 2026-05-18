package com.gymapp.ms_evaluacion.repository;

import com.gymapp.ms_evaluacion.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByActivoTrue();

    Optional<Evaluacion> findByIdAndActivoTrue(Long id);

    List<Evaluacion> findByMiembroIdAndActivoTrueOrderByFechaEvaluacionDesc(Long miembroId);
}

