package es.bcnc.demo.infrastructure.repository;

import es.bcnc.demo.infrastructure.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para la entidad {@link BrandEntity}.
 * <p>
 * Provee operaciones CRUD sobre la tabla {@code brands} sin necesidad
 * de implementación manual; Spring genera el proxy en tiempo de arranque.
 * </p>
 */
public interface JpaBrandRepository extends JpaRepository<BrandEntity, Long> {
}
