package com.cesde.petmind.exception;

// Un dato que debe ser unico ya lo tiene otro registro, como el correo de un usuario.
// El GlobalExceptionHandler responde 409
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
