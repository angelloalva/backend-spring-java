package com.neuromotion.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neuromotion.backend.exceptions.HorarioNoDisponibleException;
import com.neuromotion.backend.model.Cita;
import com.neuromotion.backend.model.Turno;
import com.neuromotion.backend.repository.CitaRepository;
import com.neuromotion.backend.repository.TurnoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaService {

 private final TurnoRepository turnoRepository;
    private final CitaRepository citaRepository;

    public Cita crearCita(Cita cita) {
        if (!estaDisponible(cita.getDoctorId(), cita.getSedeId(), cita.getFechaHora())) {
            List<LocalDateTime> horarios = obtenerHorariosDisponibles(
            cita.getDoctorId(),
            cita.getSedeId(),
            cita.getFechaHora().toLocalDate()
        );
            
        throw new HorarioNoDisponibleException("El doctor no tiene disponibilidad en esa fecha, hora o sede.", horarios);
        }
    // Buscar el turno y slot correspondiente y marcarlo como ocupado
       /*  List<Turno> turnos = turnoRepository.findByDoctorIdAndSedeId(cita.getDoctorId(), cita.getSedeId());
        for (Turno turno : turnos) {
            for (Turno.DiaTurno dia : turno.getDiasDisponibles()) {
                if (dia.getFecha().equals(cita.getFechaHora().toLocalDate())) {
                    for (Turno.SlotTurno slot : dia.getSlots()) {
                        if (slot.getFechaHoraCompleta().equals(cita.getFechaHora())) {
                            slot.setOcupado(true);
                            turnoRepository.save(turno); // Guarda el turno actualizado
                            break;
                        }
                    }
                }
            }
        }*/
    // Buscar el turno y slot correspondiente y marcarlo como ocupado (optimizado)
    List<Turno> turnos = turnoRepository.findByDoctorIdAndSedeId(cita.getDoctorId(), cita.getSedeId());
    turnos.stream()
        .flatMap(turno -> turno.getDiasDisponibles().stream()
            .filter(dia -> dia.getFecha().equals(cita.getFechaHora().toLocalDate()))
            .flatMap(dia -> dia.getSlots().stream()
                .filter(slot -> slot.getFechaHoraCompleta().equals(cita.getFechaHora()))
                .peek(slot -> {
                    slot.setOcupado(true);
                    turnoRepository.save(turno); // Guarda el turno actualizado
                })
            )
        ).findFirst();
        return citaRepository.save(cita);
    }

    public boolean estaDisponible(String doctorId, String sedeId, LocalDateTime fechaHora) {
        // 1. Obtener los turnos del doctor para la sede
        List<Turno> turnos = turnoRepository.findByDoctorIdAndSedeId(doctorId, sedeId);

        // 2. Verificar si la fecha y hora están dentro de algún turno
        boolean dentroDeTurno = turnos.stream().anyMatch(turno ->
            turno.getDiasDisponibles().stream().anyMatch(dia ->
                dia.getFecha().equals(fechaHora.toLocalDate()) &&
                !fechaHora.toLocalTime().isBefore(dia.getHoraInicio()) &&
                !fechaHora.toLocalTime().isAfter(dia.getHoraFin())
            )
        );

        if (!dentroDeTurno) {
            return false;
        }

        // 3. Verificar si ya existe cita en esa fecha y hora para el doctor
        return !citaRepository.existsByDoctorIdAndFechaHora(doctorId, fechaHora);
    }

    public List<LocalDateTime> obtenerHorariosDisponibles(String doctorId, String sedeId, LocalDate fecha) {
            List<Turno> turnos = turnoRepository.findByDoctorIdAndSedeId(doctorId, sedeId);

    return turnos.stream()
        .flatMap(turno -> turno.getDiasDisponibles().stream())
        .filter(dia -> dia.getFecha().equals(fecha))
        .flatMap(dia -> dia.getSlots().stream())
        .filter(slot -> !slot.isOcupado())
        .map(Turno.SlotTurno::getFechaHoraCompleta)
        .toList();
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