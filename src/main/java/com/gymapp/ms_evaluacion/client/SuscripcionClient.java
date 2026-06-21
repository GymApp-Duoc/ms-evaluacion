package com.gymapp.ms_evaluacion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-suscripciones")
public interface SuscripcionClient {

    @GetMapping("/api/suscripciones/miembro/{miembroId}")
    Object obtenerSuscripcionesPorMiembro(@PathVariable("miembroId") Long miembroId);
}

