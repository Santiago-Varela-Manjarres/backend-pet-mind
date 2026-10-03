package com.cesde.petmind.exception;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// Atrapa las excepciones que salen de cualquier controlador y responde con un ErrorResponse.
// Por eso los servicios solo lanzan la excepcion y los controladores no necesitan try/catch
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(ReglaNegocioException ex, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoDuplicado(RecursoDuplicadoException ex,
            HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // La base de datos rechazo el registro por una restriccion que el servicio no reviso antes, como un
    // NIT de fundacion repetido o un campo obligatorio vacio. Sin este manejador la respuesta seria un 500.
    // Hibernate ya deja la causa exacta en el log, asi que aca solo se elige el codigo
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarRestriccionBaseDatos(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        // 23505 es el codigo con el que PostgreSQL avisa que un valor unique ya existe
        if (ex.getMostSpecificCause() instanceof SQLException causa && "23505".equals(causa.getSQLState())) {
            return responder(HttpStatus.CONFLICT,
                    "Ya existe un registro con alguno de los datos que deben ser unicos", request);
        }
        return responder(HttpStatus.BAD_REQUEST,
                "Los datos no cumplen las restricciones de la base de datos: falta un dato obligatorio o alguno no es valido",
                request);
    }

    // Cualquier error que no se previo. El detalle va al log del servidor, nunca a la respuesta
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorInesperado(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado en el servidor", request);
    }

    // Los errores propios de Spring MVC (JSON mal formado, parametro faltante o con un tipo que no
    // corresponde, ruta inexistente, metodo HTTP no permitido...) los resuelve la clase padre con el
    // codigo correcto. Si el error es del servidor (5xx) se deja en el log, igual que los inesperados
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        if (statusCode.is5xxServerError()) {
            log.error("Error interno en {}", request.getDescription(false), ex);
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    // La clase padre describe esos errores en un ProblemDetail; aca se cambia por un ErrorResponse
    // para que todas las respuestas de error de la API tengan la misma forma
    @Override
    protected ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        String mensaje = (body instanceof ProblemDetail detalle) ? detalle.getDetail() : null;
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        return new ResponseEntity<>(crearError(statusCode, mensaje, servletRequest), headers, statusCode);
    }

    private ResponseEntity<ErrorResponse> responder(HttpStatus status, String mensaje, HttpServletRequest request) {
        return ResponseEntity.status(status).body(crearError(status, mensaje, request));
    }

    private ErrorResponse crearError(HttpStatusCode status, String mensaje, HttpServletRequest request) {
        String error = (status instanceof HttpStatus conocido) ? conocido.getReasonPhrase() : null;
        return new ErrorResponse(LocalDateTime.now(), status.value(), error, mensaje, request.getRequestURI());
    }
}
