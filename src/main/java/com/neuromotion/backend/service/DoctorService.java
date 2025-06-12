package com.neuromotion.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuromotion.backend.dto.DoctorCreateRequest;
import com.neuromotion.backend.dto.DoctorResponse;
import com.neuromotion.backend.dto.DoctorUpdateRequest;
import com.neuromotion.backend.dto.EspecialidadValidationResponse;
import com.neuromotion.backend.model.Doctor;
import com.neuromotion.backend.model.Especialidad;
import com.neuromotion.backend.model.Turno;
import com.neuromotion.backend.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    
  
    private final DoctorRepository doctorRepository;
    
    private final TurnoService turnoService;
    
    private final EspecialidadService especialidadService;
    
    // Validar especialidad antes de crear doctor
    public EspecialidadValidationResponse validarEspecialidad(String especialidadNombre) {
        if (especialidadNombre == null || especialidadNombre.trim().isEmpty()) {
            return new EspecialidadValidationResponse(false, "La especialidad no puede estar vacía", null, null);
        }
        
        String nombreLimpio = especialidadNombre.trim();
        List<Especialidad> todasEspecialidades = especialidadService.listar();
        
        // Buscar coincidencia exacta (ignorando mayúsculas)
        Optional<Especialidad> coincidenciaExacta = todasEspecialidades.stream()
                .filter(esp -> esp.getNombre().equalsIgnoreCase(nombreLimpio))
                .findFirst();
        
        if (coincidenciaExacta.isPresent()) {
            return new EspecialidadValidationResponse(
                true, 
                "Especialidad encontrada", 
                coincidenciaExacta.get(), 
                null
            );
        }
        
        // Buscar especialidades similares (para sugerir)
        List<Especialidad> similares = todasEspecialidades.stream()
                .filter(esp -> sonSimilares(esp.getNombre(), nombreLimpio))
                .limit(3)
                .collect(Collectors.toList());
        
        return new EspecialidadValidationResponse(
            false, 
            similares.isEmpty() ? "Especialidad no encontrada" : "Especialidad no encontrada, pero hay similares", 
            null, 
            similares
        );
    }
    
    // Método para determinar si dos nombres son similares
    private boolean sonSimilares(String nombre1, String nombre2) {
        String n1 = nombre1.toLowerCase().replaceAll("[\\s-_]", "");
        String n2 = nombre2.toLowerCase().replaceAll("[\\s-_]", "");
        
        // Verificar si uno contiene al otro o viceversa
        return n1.contains(n2) || n2.contains(n1) || 
               calcularSimilitudLevenshtein(n1, n2) > 0.7;
    }
    
    // Algoritmo básico de similitud (puedes usar librerías como Apache Commons Text)
    private double calcularSimilitudLevenshtein(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLen;
    }
    
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1), dp[i-1][j-1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }
    // Crear doctor
    public DoctorResponse crearDoctor(DoctorCreateRequest request) {
      // Validar que no exista un doctor con el mismo CMP
        if (doctorRepository.existsByCmp(request.getCmp())) {
            throw new IllegalArgumentException("Ya existe un doctor con el CMP: " + request.getCmp());
        }
        
        // Validar especialidad - debe existir previamente
        if (request.getEspecialidad() != null && !request.getEspecialidad().trim().isEmpty()) {
            EspecialidadValidationResponse validacion = validarEspecialidad(request.getEspecialidad());
            if (!validacion.isExists()) {
                throw new IllegalArgumentException(
                    "La especialidad '" + request.getEspecialidad() + "' no existe. " +
                    "Debe crearla primero o usar una existente."
                );
            }
            // Usar el nombre exacto de la especialidad encontrada para consistencia
            request.setEspecialidad(validacion.getEspecialidadEncontrada().getNombre());
        }
        Doctor doctor = new Doctor();
        doctor.setNombres(request.getNombres());
        doctor.setApellidos(request.getApellidos());
        doctor.setCmp(request.getCmp());
        doctor.setEspecialidad(request.getEspecialidad());
        doctor.setSedeId(request.getSedeId());
        doctor.setFotoUrl(request.getFotoUrl());
      
        
        Doctor doctorGuardado = doctorRepository.save(doctor);
        return DoctorResponse.fromDoctor(doctorGuardado);
    }
    
    // Obtener todos los doctores
    public List<DoctorResponse> obtenerTodosLosDoctores() {
        return doctorRepository.findAll().stream()
                .map(DoctorResponse::fromDoctor)
                .collect(Collectors.toList());
    }
    
    // Obtener doctor por ID
    public Optional<DoctorResponse> obtenerDoctorPorId(String id) {
        return doctorRepository.findById(id)
                .map(DoctorResponse::fromDoctor);
    }
    
    // Obtener doctor por CMP
    public Optional<DoctorResponse> obtenerDoctorPorCmp(String cmp) {
        return doctorRepository.findByCmp(cmp)
                .map(DoctorResponse::fromDoctor);
    }
    
    // Obtener doctores por especialidad
    public List<DoctorResponse> obtenerDoctoresPorEspecialidad(String especialidad) {
        return doctorRepository.findByEspecialidad(especialidad).stream()
                .map(DoctorResponse::fromDoctor)
                .collect(Collectors.toList());
    }
    
    // Obtener doctores por sede
    public List<DoctorResponse> obtenerDoctoresPorSede(String sedeId) {
        return doctorRepository.findBySedeId(sedeId).stream()
                .map(DoctorResponse::fromDoctor)
                .collect(Collectors.toList());
    }
    
    // Buscar doctores por nombre
    public List<DoctorResponse> buscarDoctoresPorNombre(String nombre) {
        return doctorRepository.findByNombresContainingIgnoreCase(nombre).stream()
                .map(DoctorResponse::fromDoctor)
                .collect(Collectors.toList());
    }
    
    // Actualizar doctor
    public Optional<DoctorResponse> actualizarDoctor(String id, DoctorUpdateRequest request) {
        return doctorRepository.findById(id).map(doctor -> {
            if (request.getNombres() != null) {
                doctor.setNombres(request.getNombres());
            }
            if (request.getApellidos() != null) {
                doctor.setApellidos(request.getApellidos());
            }
            if (request.getEspecialidad() != null) {
                // Validar la nueva especialidad
                EspecialidadValidationResponse validacion = validarEspecialidad(request.getEspecialidad());
                if (!validacion.isExists()) {
                    throw new IllegalArgumentException(
                        "La especialidad '" + request.getEspecialidad() + "' no existe. " +
                        "Debe crearla primero o usar una existente."
                    );
                }
                doctor.setEspecialidad(validacion.getEspecialidadEncontrada().getNombre());
            }
            if (request.getSedeId() != null) {
                doctor.setSedeId(request.getSedeId());
            }
            if (request.getFotoUrl() != null) {
                doctor.setFotoUrl(request.getFotoUrl());
            }
            
            Doctor doctorActualizado = doctorRepository.save(doctor);
            return DoctorResponse.fromDoctor(doctorActualizado);
        });
    }
    
    // Eliminar doctor
    public boolean eliminarDoctor(String id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Obtener turnos de un doctor (método actualizado)
    public List<Turno> obtenerTurnosDeDoctor(String doctorId) {
        // No usar los turnos del doctor directamente, sino consultar al TurnoService
        // que tiene la información más actualizada
        return turnoService.obtenerTurnosPorDoctor(doctorId);
    }
}