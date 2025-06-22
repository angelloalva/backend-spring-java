package com.neuromotion.backend.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.neuromotion.backend.enums.Rol;

import lombok.Data;

@Data
public class PerfilResponseDTO {
    
    // Atributos del perfil
    private String id;
    private String tipoDocumento; // DNI, CE, PASAPORTE, etc.
    private String numeroDocumento; // 12345678, X12345, etc.
    private String nombres;
    private String apellidos;
    private String celular;
    private String correo;
    private String direccion;

    private Set<String> roles;

    public PerfilResponseDTO(String id, String tipoDocumento, String numeroDocumento, String nombres,
            String apellidos,
            String celular,
            String correo, 
            String direccion,
            Set<Rol> roles) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
        this.direccion = direccion;

        this.roles = roles.stream().map(Rol::name).collect(Collectors.toSet());
    }
}
