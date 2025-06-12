package com.neuromotion.backend.dto;

import com.neuromotion.backend.model.Doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private String id;
    private String nombres;
    private String cmp;
    private String especialidad;
    private String sedeId;
    private String fotoUrl;

    
    public static DoctorResponse fromDoctor(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setNombres(doctor.getNombres());
        response.setCmp(doctor.getCmp());
        response.setEspecialidad(doctor.getEspecialidad());
        response.setSedeId(doctor.getSedeId());
        response.setFotoUrl(doctor.getFotoUrl());
      
        return response;
    }
}