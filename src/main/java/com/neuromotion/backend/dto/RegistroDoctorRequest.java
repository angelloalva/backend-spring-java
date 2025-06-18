package com.neuromotion.backend.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class RegistroDoctorRequest {
    @Valid
    private RegistroRequest usuario;
    @Valid
    private DoctorCreateRequest doctor;
}