package com.cesde.petmind.exception;

// Los datos que llegaron incumplen una regla de negocio o les falta un dato obligatorio.
// El GlobalExceptionHandler responde 400
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
