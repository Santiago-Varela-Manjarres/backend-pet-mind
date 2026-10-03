package com.cesde.petmind.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Cuerpo JSON de todas las respuestas de error de la API.
// Lleva los mismos campos que el error por defecto de Spring Boot
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    // Texto del codigo HTTP, por ejemplo "Not Found" para el 404
    private String error;

    private String message;

    // Ruta que se pidio, por ejemplo /api/usuarios/99
    private String path;
}
