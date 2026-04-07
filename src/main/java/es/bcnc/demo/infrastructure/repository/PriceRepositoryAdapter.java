package es.bcnc.demo.infrastructure.repository;

import es.bcnc.demo.domain.model.Price;
import es.bcnc.demo.domain.port.PriceRepositoryPort;
import es.bcnc.demo.infrastructure.entity.PriceEntity;
import es.bcnc.demo.infrastructure.exception.PriceNotFoundException;
import es.bcnc.demo.infrastructure.mapper.PriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Adaptador de repositorio que implementa el puerto de salida
 * {@link PriceRepositoryPort}.
 * <p>
 * Delega la consulta en {@link JpaPriceRepository} (LIMIT 1 en la
 * query) y convierte la entidad al modelo de dominio mediante
 * {@link PriceMapper}. La caché Caffeine se aplica aquí, en la capa
 * de infraestructura, porque es una decisión técnica de acceso a
 * datos, no de lógica de negocio.
 * </p>
 */
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = "prices")
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    /** Repositorio JPA para la consulta de precios. */
    private final JpaPriceRepository jpaPriceRepository;

    /** Mapeador entre entidad JPA y modelo de dominio. */
    private final PriceMapper priceMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Aplica caché Caffeine sobre el resultado. No está diseñado
     * para ser sobreescrito.
     * </p>
     */
    @Override
    @Cacheable(key = "#brandId + '-' + #productId + '-' + #applicationDate")
    public Price findApplicablePrice(
            final Long productId,
            final Long brandId,
            final LocalDateTime applicationDate) {
        PriceEntity price = jpaPriceRepository
                .findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(
                        String.format(
                                "No price found for brandId=%d,"
                                + " productId=%d, date=%s",
                                brandId, productId, applicationDate)));

        return priceMapper.toDomain(price);
    }
}
