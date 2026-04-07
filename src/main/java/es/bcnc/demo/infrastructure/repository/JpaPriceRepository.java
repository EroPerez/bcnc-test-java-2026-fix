package es.bcnc.demo.infrastructure.repository;

import es.bcnc.demo.infrastructure.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio JPA para el acceso a la tabla {@code prices}.
 * <p>
 * Utiliza {@code JOIN FETCH p.brand} para cargar la marca en el
 * mismo SELECT (estrategia anti-N+1) y {@code LIMIT 1} para que
 * la base de datos devuelva únicamente la tarifa de mayor prioridad,
 * sin transferir filas innecesarias.
 * </p>
 */
@Repository
public interface JpaPriceRepository
        extends JpaRepository<PriceEntity, Long> {

    /**
     * Devuelve la tarifa de mayor prioridad aplicable para una marca
     * y producto en la fecha indicada, con la marca ya inicializada
     * en el mismo SELECT.
     *
     * @param brandId         identificador de la marca
     * @param productId       identificador del producto
     * @param applicationDate fecha y hora de consulta
     * @return la tarifa de mayor prioridad, o
     *         {@link Optional#empty()} si no existe ninguna
     */
    @Query("SELECT p FROM PriceEntity p JOIN FETCH p.brand "
            + "WHERE p.brand.id = :brandId"
            + " AND p.productId = :productId "
            + "AND :applicationDate BETWEEN p.startDate AND p.endDate "
            + "ORDER BY p.priority DESC LIMIT 1")
    Optional<PriceEntity> findApplicablePrice(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate);
}
