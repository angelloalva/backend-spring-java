package com.neuromotion.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.neuromotion.backend.model.Doctor;

public interface DoctorRepository  extends MongoRepository<Doctor, String> {
    // Búsquedas básicas
    Optional<Doctor> findByCmp(String cmp);
    List<Doctor> findByEspecialidad(String especialidad);
    List<Doctor> findBySedeId(String sedeId);
    List<Doctor> findByEspecialidadAndSedeId(String especialidad, String sedeId);
    
    // Búsquedas por nombre (case insensitive)
    List<Doctor> findByNombresContainingIgnoreCase(String nombres);
    
    // Verificar existencia
    boolean existsByCmp(String cmp);
    
    // Consultas personalizadas
    @Query("{ 'especialidad': ?0, 'turnos': { $exists: true, $not: { $size: 0 } } }")
    List<Doctor> findByEspecialidadWithTurnos(String especialidad);
}