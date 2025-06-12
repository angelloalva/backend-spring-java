package com.neuromotion.backend.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neuromotion.backend.model.Cita;
import com.neuromotion.backend.model.Turno;
import com.neuromotion.backend.repository.CitaRepository;
import com.neuromotion.backend.repository.TurnoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaService {

 private final TurnoRepository turnoRepository;
    private final CitaRepository citaRepository;

    public Cita crearCita(Cita cita) {
        if (!estaDisponible(cita.getDoctorId(), cita.getSedeId(), cita.getFechaHora())) {
            throw new IllegalArgumentException("El doctor no tiene disponibilidad en esa fecha, hora o sede.");
        }

        return citaRepository.save(cita);
    }

    public boolean estaDisponible(String doctorId, String sedeId, OffsetDateTime fechaHora) {
        // 1. Obtener los turnos del doctor para la sede y día
        List<Turno> turnos = turnoRepository.findByDoctorIdAndSedeId(doctorId, sedeId);

        boolean dentroDeTurno = true;

        if (!dentroDeTurno) {
            return false;
        }

        // 2. Verificar si ya existe cita en esa fecha y hora para el doctor
        return !citaRepository.existsByDoctorIdAndFechaHora(doctorId, fechaHora);
    }

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> buscarPorId(String id) {
        return citaRepository.findById(id);
    }

    public List<Cita> listarPorPaciente(String pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> listarPorDoctor(String doctorId) {
        return citaRepository.findByDoctorId(doctorId);
    }

    public void eliminarCita(String id) {
        citaRepository.deleteById(id);
    }

}