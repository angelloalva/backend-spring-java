package com.neuromotion.backend.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.neuromotion.backend.enums.EstadoCita;
import com.neuromotion.backend.model.Cita;
import com.neuromotion.backend.model.Doctor;
import com.neuromotion.backend.model.Especialidad;
import com.neuromotion.backend.model.Sede;
import com.neuromotion.backend.model.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CitaResponse {
    private String id;
    private String pacienteId;
    private String sedeId; 
    private String sedeNombre;// Opcional, si quieres incluir la sede
    private String turnoId; // Opcional, si quieres incluir el turno asignado
    private String observaciones; // Campo para notas adicionaleso
    private String doctorId;
    private String doctorNombres;
    private String doctorApellidos;
    private String especialidadNombre;
    private LocalDateTime fechaHora;
    private EstadoCita estado;

    // Constructor, getters, setters, y builder
    public static CitaResponse fromCita(Cita cita, Usuario user,Especialidad esp,Sede sede) {
        return CitaResponse.builder()
                .id(cita.getId())
                .pacienteId(cita.getPacienteId())
                .sedeId(cita.getSedeId())
                .turnoId(cita.getTurnoId()) 
                .observaciones(cita.getObservaciones())
                .doctorId(cita.getDoctorId())
                .especialidadNombre(esp != null ? esp.getNombre() : "No disponible")
                .sedeNombre(sede != null ? sede.getNombre() : "No disponible")
                .doctorNombres(user != null ? user.getNombres() : "Desconocido")
                .doctorApellidos(user != null ? user.getApellidos() : "")
                .fechaHora(cita.getFechaHora())
                .estado(cita.getEstado())
                .build();
    }
}