package com.gymapp.ms_evaluacion.controller;

import com.gymapp.ms_evaluacion.dto.EvaluacionRequestDTO;
import com.gymapp.ms_evaluacion.dto.EvaluacionResponseDTO;
import com.gymapp.ms_evaluacion.service.EvaluacionService;
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
public class EvaluacionController {

    private final EvaluacionService service;

    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerTodas() {
        log.info("[CONTROLLER] Solicitando lista de todas las evaluaciones activas.");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("[CONTROLLER] Buscando evaluación por ID: {}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerPorMiembro(@PathVariable Long miembroId) {
        log.info("[CONTROLLER] Consultando historial de evaluación para el miembro ID: {}", miembroId);
        return ResponseEntity.ok(service.obtenerHistorialPorMiembro(miembroId));
    }

    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crear(@Valid @RequestBody EvaluacionRequestDTO dto) {
        log.info("[CONTROLLER] Petición para crear nueva evaluación recibida. Miembro ID: {}", dto.getMiembroId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EvaluacionRequestDTO dto) {
        log.info("[CONTROLLER] Solicitud de actualización para evaluación ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("[CONTROLLER] Petición de eliminación lógica para evaluación ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}