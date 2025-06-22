package com.neuromotion.backend.dto;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String message;
    private UsuarioRequest user;

    public RegisterResponseDTO(String message, UsuarioRequest user) {
        this.message = message;
        this.user = user;
    }
}