package com.neuromotion.backend.service;

import com.neuromotion.backend.dto.MensajeResponse;
import com.neuromotion.backend.dto.SedeRequest;
import com.neuromotion.backend.dto.SedeResponse;
import com.neuromotion.backend.exceptions.DocumentoNoEncontradoException;
import com.neuromotion.backend.model.Sede;
import com.neuromotion.backend.repository.SedeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SedeService {

    
    private final SedeRepository sedeRepository;

    // Crear una nueva sede a partir de un DTO de creación
    public ResponseEntity<MensajeResponse> crearSede(SedeRequest request) {
        try {
            Sede sede = new Sede();
            sede.setNombre(request.getNombre());
            sede.setDireccion(request.getDireccion());
            sede = sedeRepository.save(sede);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MensajeResponse("Sede creado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeResponse("Error al crear sede"));
        
        }
       
    }

    // Obtener una sede por ID
    public ResponseEntity<?> obtenerSedePorId(String id) {
        Optional<Sede> sede = sedeRepository.findById(id);
        if (sede.isEmpty()) {
           throw new DocumentoNoEncontradoException("Sede no encontrado");
        }
        Sede actual = sede.get();
        return ResponseEntity.ok(actual);
    }

    // Obtener todas las sedes
    public List<Sede> obtenerTodasLasSedes() {
        return sedeRepository.findAll();
    }

    // Actualizar una sede parcialmente
    public ResponseEntity<MensajeResponse> actualizarSede(String id, SedeRequest request) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new DocumentoNoEncontradoException("Sede no encontrada con ID: " + id));
        if (request.getNombre() != null) {
            sede.setNombre(request.getNombre());
        }
        if (request.getDireccion() != null) {
            sede.setDireccion(request.getDireccion());
        }
        sede = sedeRepository.save(sede);
       // return new SedeResponse(sede.getId(), sede.getNombre(), sede.getDireccion());
       return ResponseEntity.ok(new MensajeResponse("Actualizado con éxito"));
    }

    // Eliminar una sede por ID
    public void eliminarSede(String id) {
        if (!sedeRepository.existsById(id)) {
            throw new DocumentoNoEncontradoException("Sede no encontrada con ID: " + id);
        }
        sedeRepository.deleteById(id);
    }
}