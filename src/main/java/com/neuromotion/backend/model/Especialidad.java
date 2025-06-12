package com.neuromotion.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "especialidades")
public class Especialidad {

    @Id
    private String id; // Por ejemplo: "cardiologia", "pediatria"
    private String nombre; // Ejemplo: "Cardiología"
    private String descripcion;
}