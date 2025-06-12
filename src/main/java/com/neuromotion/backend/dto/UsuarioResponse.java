package com.neuromotion.backend.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.neuromotion.backend.enums.Rol;

import lombok.Data;

@Data
public class UsuarioResponse{
private String id;
    private String nombres;
    private String apellidos;
    private String celular;
    private String correo;
    private String direccion;
    private String tipoDocumento;
    private String numeroDocumento;private Set<String> roles;

    public UsuarioResponse(String id,String nombres, String apellidos, String celular, 
                             String correo, String direccion, String tipoDocumento, 
                             String numeroDocumento, Set<Rol> roles) {
                                this.id=id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.correo = correo;
        this.direccion = direccion;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;  this.roles = roles.stream().map(Rol::name).collect(Collectors.toSet());
    }
}