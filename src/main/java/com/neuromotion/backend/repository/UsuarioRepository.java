package com.neuromotion.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.neuromotion.backend.enums.Rol;
import com.neuromotion.backend.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    Optional<Usuario> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);
    
    List<Usuario> findByNombresContainingIgnoreCaseAndRolesContaining(String nombres, Rol rol);

}