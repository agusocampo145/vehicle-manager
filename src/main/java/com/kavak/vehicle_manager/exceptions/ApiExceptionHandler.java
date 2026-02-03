package com.kavak.vehicle_manager.exceptions;

import com.kavak.vehicle_manager.exceptions.api.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(ReglaDeNegocioException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Para reglas de negocio que hoy estén lanzadas como IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Cubre enums inválidos en JSON, JSON mal formado, etc.
     * Ej: tipoMantenimiento: "OTRO" cuando no existe en el enum.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        String message = "Body inválido. Verifique el JSON enviado";
        return build(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Cubre casos de mismatch de tipos esperados.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Parámetro inválido: '" + ex.getName() + "'. Valor recibido: '" + ex.getValue() + "'.";
        return build(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Cubre @Valid en DTOs (por ejemplo patente vacía, kilometraje negativo, etc.)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> "Campo '" + err.getField() + "': " + err.getDefaultMessage())
                .orElse("Validación inválida");
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Error inesperado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.name(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}
