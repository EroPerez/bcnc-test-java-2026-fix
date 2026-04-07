package es.bcnc.demo.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para los controladores REST.
 * <p>
 * Intercepta las excepciones lanzadas en cualquier controlador y
 * las convierte en respuestas HTTP con estructura uniforme
 * (timestamp, status, error, message). Gestiona las siguientes
 * categorías de error:
 * <ul>
 *   <li>{@link PriceNotFoundException} → 404 Not Found</li>
 *   <li>{@link BrandNotFoundException} → 404 Not Found</li>
 *   <li>{@link MethodArgumentTypeMismatchException}
 *       → 400 Bad Request</li>
 *   <li>{@link MethodArgumentNotValidException}
 *       → 400 Bad Request con detalle por campo</li>
 *   <li>{@link Exception} (genérica)
 *       → 500 Internal Server Error</li>
 * </ul>
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Clave del campo timestamp en el cuerpo de error. */
    private static final String KEY_TIMESTAMP = "timestamp";

    /** Clave del campo status en el cuerpo de error. */
    private static final String KEY_STATUS    = "status";

    /** Clave del campo error en el cuerpo de error. */
    private static final String KEY_ERROR     = "error";

    /** Clave del campo message en el cuerpo de error. */
    private static final String KEY_MESSAGE   = "message";

    /**
     * Maneja la excepción lanzada cuando no se encuentra ninguna
     * tarifa aplicable.
     *
     * @param ex excepción de precio no encontrado
     * @return respuesta 404 con detalle del error
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePriceNotFound(
            final PriceNotFoundException ex) {
        return notFoundBody(ex.getMessage());
    }

    /**
     * Maneja la excepción lanzada cuando no se encuentra la marca
     * solicitada.
     *
     * @param ex excepción de marca no encontrada
     * @return respuesta 404 con detalle del error
     */
    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBrandNotFound(
            final BrandNotFoundException ex) {
        return notFoundBody(ex.getMessage());
    }

    /**
     * Maneja errores de conversión de tipo en parámetros de query
     * (p. ej. fecha inválida).
     *
     * @param ex excepción de tipo incorrecto en parámetro
     * @return respuesta 400 con detalle del error
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            final MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_TIMESTAMP, LocalDateTime.now());
        body.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(KEY_ERROR, "Bad Request");
        body.put(KEY_MESSAGE,
                String.format("Invalid value '%s' for parameter '%s'",
                        ex.getValue(), ex.getName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maneja los errores de validación de Bean Validation en los
     * parámetros de entrada.
     * <p>
     * Incluye en la respuesta el detalle de cada campo que no ha
     * superado la validación.
     * </p>
     *
     * @param ex excepción de validación de argumentos
     * @return respuesta 400 con detalle por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            final MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_TIMESTAMP, LocalDateTime.now());
        body.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(KEY_ERROR, "Bad Request");
        body.put(KEY_MESSAGE, "Validation error");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        body.put("details", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Captura cualquier excepción no controlada y devuelve un error
     * genérico del servidor.
     *
     * @param ex excepción no controlada
     * @return respuesta 500 con detalle del error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            final Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_TIMESTAMP, LocalDateTime.now());
        body.put(KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(KEY_ERROR, "Internal Server Error");
        body.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Construye el cuerpo de respuesta 404 con el mensaje dado.
     *
     * @param message descripción del recurso no encontrado
     * @return respuesta 404 con estructura uniforme
     */
    private ResponseEntity<Map<String, Object>> notFoundBody(
            final String message) {
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_TIMESTAMP, LocalDateTime.now());
        body.put(KEY_STATUS, HttpStatus.NOT_FOUND.value());
        body.put(KEY_ERROR, "Not Found");
        body.put(KEY_MESSAGE, message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
