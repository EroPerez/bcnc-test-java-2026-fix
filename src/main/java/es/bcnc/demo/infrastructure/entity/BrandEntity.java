package es.bcnc.demo.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que mapea la tabla {@code brands} de la base de datos.
 * <p>
 * Representa una cadena o grupo de marcas tal como se almacena en la capa de
 * infraestructura. El identificador no es autogenerado: se usa el valor de
 * negocio directamente (p. ej. {@code 1 = ZARA}).
 * </p>
 */
@Entity
@Table(name = "brands")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {

    /** Identificador único de la cadena/marca (clave de negocio). */
    @Id
    private Long id;

    /** Nombre comercial de la cadena (p. ej. "ZARA"). */
    private String name;
}
