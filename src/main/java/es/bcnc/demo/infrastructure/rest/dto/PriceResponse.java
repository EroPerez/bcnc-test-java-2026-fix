package es.bcnc.demo.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de salida con el resultado de la consulta del precio aplicable.
 * <p>
 * Representa la tarifa vigente para la combinación de marca, producto
 * y fecha indicada en la petición. Las fechas se serializan con el
 * patrón {@code yyyy-MM-dd HH:mm:ss}.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tarifa de precio aplicable para la combinación"
        + " de marca, producto y fecha indicados")
public class PriceResponse {

    /** Identificador del producto al que corresponde la tarifa. */
    @Schema(description = "Identificador del producto", example = "35455")
    private Long productId;

    /** Identificador de la cadena o marca. */
    @Schema(description = "Identificador de la cadena/marca",
            example = "1")
    private Long brandId;

    /** Nombre comercial de la cadena/marca (p. ej. "ZARA"). */
    @Schema(description = "Nombre comercial de la cadena/marca",
            example = "ZARA")
    private String brandName;

    /** Identificador de la lista de precios (tarifa) aplicada. */
    @Schema(
            description = "Identificador de la lista de precios"
                    + " (tarifa) aplicada",
            example = "1")
    private Long priceList;

    /** Fecha y hora de inicio de la vigencia de la tarifa. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Fecha y hora de inicio de vigencia"
                    + " de la tarifa",
            example = "2020-06-14 00:00:00")
    private LocalDateTime startDate;

    /** Fecha y hora de fin de la vigencia de la tarifa. */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Fecha y hora de fin de vigencia"
                    + " de la tarifa",
            example = "2020-12-31 23:59:59")
    private LocalDateTime endDate;

    /** Precio final de venta al público (PVP) en la moneda. */
    @Schema(
            description = "Precio final de venta al público (PVP)",
            example = "35.50")
    private Double price;
}
