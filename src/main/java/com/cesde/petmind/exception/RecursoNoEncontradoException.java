package com.cesde.petmind.exception;

// El registro pedido no existe o fue borrado logicamente. El GlobalExceptionHandler responde 404
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
