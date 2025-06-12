package com.neuromotion.backend.repository;


import com.neuromotion.backend.model.Sede;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SedeRepository extends MongoRepository<Sede, String> {
}