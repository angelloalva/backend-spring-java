package com.neuromotion.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.neuromotion.backend.model.Cita;
import com.neuromotion.backend.service.CitaService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<?> crearCita(@Valid @RequestBody Cita cita) {
        try {
            Cita creada = citaService.crearCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Cita>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable String id) {
        return citaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> listarPorPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(citaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Cita>> listarPorDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(citaService.listarPorDoctor(doctorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable String id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}