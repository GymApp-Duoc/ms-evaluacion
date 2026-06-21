package com.gymapp.ms_evaluacion.controller;

import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.service.EvaluacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
@Tag(name = "Evaluaciones Físicas", description = "Operaciones de registro y análisis del estado físico de los miembros")
public class EvaluacionController {

    private final EvaluacionService service;

    @Operation(summary = "Obtener todas las evaluaciones activas")
    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar evaluación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Reporte 2: Consultar el historial cronológico de un miembro")
    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(service.obtenerHistorialPorMiembro(miembroId));
    }

    @Operation(summary = "Registrar nueva evaluación física")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evaluación creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, miembro no existe o sin suscripción activa")
    })
    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crear(@Valid @RequestBody EvaluacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar métricas de una evaluación")
    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EvaluacionRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Borrado lógico de una evaluación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Reporte 1: Conteo total de evaluaciones registradas en el sistema")
    @GetMapping("/reportes/total")
    public ResponseEntity<Long> contarEvaluaciones() {
        return ResponseEntity.ok(service.contarEvaluacionesActivas());
    }

    @Operation(summary = "Reporte 3: Alerta médica - Listar miembros que superan X porcentaje de grasa corporal")
    @GetMapping("/reportes/riesgo")
    public ResponseEntity<List<EvaluacionResponseDTO>> listarRiesgoGrasa(@RequestParam(defaultValue = "30.0") Double limiteGrasa) {
        return ResponseEntity.ok(service.listarEvaluacionesRiesgo(limiteGrasa));
    }

    @Operation(summary = "Reporte 4: Evaluaciones recientes (últimos X días)")
    @GetMapping("/reportes/recientes")
    public ResponseEntity<List<EvaluacionResponseDTO>> listarRecientes(@RequestParam(defaultValue = "15") int dias) {
        return ResponseEntity.ok(service.listarEvaluacionesRecientes(dias));
    }

    @Operation(summary = "Reporte 5: Rendimiento y cantidad de evaluaciones tomadas por un entrenador específico")
    @GetMapping("/reportes/entrenador/{evaluadorId}/total")
    public ResponseEntity<Long> contarPorEntrenador(@PathVariable Long evaluadorId) {
        return ResponseEntity.ok(service.contarEvaluacionesPorEntrenador(evaluadorId));
    }
}