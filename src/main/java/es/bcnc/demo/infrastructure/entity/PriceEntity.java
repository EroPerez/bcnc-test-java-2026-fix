package es.bcnc.demo.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA que mapea la tabla {@code prices} de la base de datos.
 * <p>
 * La relación con {@link BrandEntity} se declara como
 * {@code @ManyToOne LAZY}: JPA no carga la marca hasta que se accede
 * a ella explícitamente. Para evitar el problema N+1 (una consulta
 * extra por cada precio), el repositorio usa
 * {@code JOIN FETCH p.brand} en su JPQL, lo que genera un único SQL
 * con JOIN y deja el campo completamente inicializado dentro de la
 * transacción.
 * </p>
 */
@Entity
@Table(name = "prices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntity {

    /** Identificador único autogenerado del registro en la tabla. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cadena/marca a la que pertenece la tarifa.
     * <p>
     * Fetch {@code LAZY} por defecto: la carga real se fuerza en
     * la consulta mediante {@code JOIN FETCH} para evitar consultas
     * adicionales.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    /** Fecha y hora de inicio de la vigencia de la tarifa. */
    private LocalDateTime startDate;

    /** Fecha y hora de fin de la vigencia de la tarifa. */
    private LocalDateTime endDate;

    /** Identificador de la lista de precios (tarifa). */
    private Long priceList;

    /** Identificador del producto al que aplica esta tarifa. */
    private Long productId;

    /**
     * Prioridad de la tarifa: a mayor valor, mayor prioridad
     * en caso de solapamiento.
     */
    private Integer priority;

    /** Precio final de venta al público (PVP). */
    private Double price;

    /** Código ISO de la moneda (p. ej. "EUR"). */
    private String currency;
}
