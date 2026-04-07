package es.bcnc.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio que representa una cadena o grupo de marcas.
 * <p>
 * Cada marca tiene un identificador único y un nombre comercial
 * (p. ej. {@code 1 = ZARA}).
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    /** Identificador único de la cadena/marca. */
    private Long id;

    /** Nombre comercial de la cadena (p. ej. "ZARA"). */
    private String name;
}
