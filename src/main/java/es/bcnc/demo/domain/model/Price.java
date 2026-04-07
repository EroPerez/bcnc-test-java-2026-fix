package es.bcnc.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa una tarifa de precio aplicable
 * a un producto.
 * <p>
 * Contiene la información de la tarifa vigente en un rango de fechas
 * para una combinación de marca y producto, con un nivel de prioridad
 * que determina cuál se aplica cuando varias tarifas se solapan.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    /** Identificador de la cadena o marca (p. ej. 1 = ZARA). */
    private Long brandId;

    /** Fecha y hora de inicio de la vigencia de la tarifa. */
    private LocalDateTime startDate;

    /** Fecha y hora de fin de la vigencia de la tarifa. */
    private LocalDateTime endDate;

    /** Identificador de la lista de precios (tarifa). */
    private Long priceList;

    /** Identificador del producto al que aplica esta tarifa. */
    private Long productId;

    /** Prioridad de la tarifa: a mayor valor, mayor prioridad. */
    private Integer priority;

    /** Precio final de venta al público (PVP). */
    private Double price;

    /** Código ISO de la moneda (p. ej. "EUR"). */
    private String currency;

    /**
     * Nombre comercial de la cadena/marca (p. ej. "ZARA"),
     * resuelto a partir de {@code brandId}.
     */
    private String brandName;
}
