package com.neuromotion.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateRequest {


    private String usuarioId;

    @NotBlank(message = "El CMP es obligatorio")
    @Pattern(regexp = "^[0-9]{4,6}$", message = "El CMP debe tener entre 4 y 6 dígitos")
    private String cmp;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidadId;

    @NotEmpty(message = "Debe asignar al menos una sede")
    private List<String> sedeIds;

    private String fotoUrl;
}