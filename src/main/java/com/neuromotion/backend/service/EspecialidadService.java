package com.neuromotion.backend.service;


import com.neuromotion.backend.dto.EspecialidadRequest;
import com.neuromotion.backend.exceptions.DocumentoNoEncontradoException;
import com.neuromotion.backend.exceptions.DuplicateResourceException;
import com.neuromotion.backend.model.Especialidad;
import com.neuromotion.backend.repository.EspecialidadRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public List<Especialidad> listar() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> buscarPorId(String id) {
        return especialidadRepository.findById(id);
    }

    public Especialidad crear(EspecialidadRequest especialidad) {
        boolean existe = especialidadRepository.existsByNombreIgnoreCase(especialidad.getNombre());
        if (existe) {
             throw new DuplicateResourceException("La especialidad con nombre '" + especialidad.getNombre() + "' ya existe");
        }
        Especialidad cesp= new Especialidad();
        cesp.setNombre(especialidad.getNombre());
        cesp.setDescripcion(especialidad.getDescripcion());
        return especialidadRepository.save(cesp);
    }

    public void eliminar(String id) {
        boolean existe = especialidadRepository.existsById(id);
         if (!existe) {
           throw new DocumentoNoEncontradoException("Especialidad con ID " + id + " no encontrada");
        }
        especialidadRepository.deleteById(id);
    }

    public Especialidad actualizar(String id,EspecialidadRequest especialidad) {
      // Buscar la especialidad existente
        Especialidad uesp = especialidadRepository.findById(id)
            .orElseThrow(() -> new DocumentoNoEncontradoException("Especialidad con ID " + id + " no encontrada"));

        // Actualizar los campos
        uesp.setNombre(especialidad.getNombre());
        uesp.setDescripcion(especialidad.getDescripcion());

        // Guardar y devolver
        return especialidadRepository.save(uesp);
    }
}