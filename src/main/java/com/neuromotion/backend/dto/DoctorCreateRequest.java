package com.neuromotion.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateRequest {
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    private String nombres;
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String apellidos;
    @NotBlank(message = "El CMP es obligatorio")
    @Pattern(regexp = "^[0-9]{4,6}$", message = "El CMP debe tener entre 4 y 6 dígitos")
    private String cmp;
    
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;
    
    @NotBlank(message = "La sede es obligatoria")
    private String sedeId;
    
    private String fotoUrl;
}