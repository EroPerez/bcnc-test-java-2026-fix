package es.bcnc.demo.infrastructure.rest.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de entrada para la consulta del precio aplicable.
 * <p>
 * Agrupa los parámetros de la petición HTTP
 * {@code GET /api/prices}. Todos los campos son obligatorios;
 * su ausencia provoca un error de validación HTTP 400 gestionado
 * por {@code GlobalExceptionHandler}.
 * </p>
 */
@Data
@Schema(description =
        "Parámetros de consulta para obtener la tarifa aplicable")
public class PriceRequest {

    /**
     * Fecha y hora para la que se desea conocer el precio vigente.
     * Formato esperado: {@code yyyy-MM-dd'T'HH:mm:ss}.
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de aplicación."
                    + " Formato: yyyy-MM-dd'T'HH:mm:ss",
            example = "2020-06-14T10:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime applicationDate;

    /** Identificador del producto a consultar. */
    @NotNull
    @Schema(
            description = "Identificador del producto",
            example = "35455",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productId;

    /** Identificador de la cadena o marca (p. ej. 1 = ZARA). */
    @NotNull
    @Schema(
            description = "Identificador de la cadena/marca"
                    + " (ej. 1 = ZARA)",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long brandId;
}
