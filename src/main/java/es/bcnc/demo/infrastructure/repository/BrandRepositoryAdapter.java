package es.bcnc.demo.infrastructure.repository;

import es.bcnc.demo.domain.model.Brand;
import es.bcnc.demo.domain.port.BrandRepositoryPort;
import es.bcnc.demo.infrastructure.exception.BrandNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador de infraestructura que implementa
 * {@link BrandRepositoryPort} con Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class BrandRepositoryAdapter implements BrandRepositoryPort {

    /** Repositorio JPA para el acceso a marcas. */
    private final JpaBrandRepository jpaBrandRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Lanza {@link es.bcnc.demo.infrastructure.exception
     * .BrandNotFoundException} si el id es nulo o no existe.
     * No está diseñado para ser sobreescrito.
     * </p>
     */
    @Override
    public Brand findById(final Long id) {
        if (id == null) {
            throw new BrandNotFoundException(null);
        }
        return jpaBrandRepository.findById(id)
                .map(entity -> Brand.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build())
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
