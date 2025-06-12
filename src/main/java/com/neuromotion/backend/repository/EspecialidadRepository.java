package com.neuromotion.backend.repository;


import com.neuromotion.backend.model.Especialidad;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EspecialidadRepository extends MongoRepository<Especialidad, String> {

    boolean existsByNombreIgnoreCase(String nombre);

}