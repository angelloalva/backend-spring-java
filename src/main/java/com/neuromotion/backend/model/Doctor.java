package com.neuromotion.backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
@Document(collection = "doctores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    private String id;

    private String nombres;
    private String apellidos;
    private String cmp;
    private String especialidad;
    private String sedeId; // Referencia al id de la sede

    private String fotoUrl; // ruta o URL de la imagen

}