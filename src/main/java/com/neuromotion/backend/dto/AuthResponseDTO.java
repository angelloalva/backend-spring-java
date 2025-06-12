package com.neuromotion.backend.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String jwt;
    private UsuarioResponse user;

    public AuthResponseDTO(String jwt, UsuarioResponse user) {
        this.jwt = jwt;
        this.user = user;
    
}}