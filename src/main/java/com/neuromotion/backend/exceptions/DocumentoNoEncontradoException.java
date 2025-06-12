package com.neuromotion.backend.exceptions;


public class DocumentoNoEncontradoException extends RuntimeException {
    public DocumentoNoEncontradoException(String message) {
        super(message);
    }
}
