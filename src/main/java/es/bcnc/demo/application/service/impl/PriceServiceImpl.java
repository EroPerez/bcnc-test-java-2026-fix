package es.bcnc.demo.application.service.impl;

import es.bcnc.demo.application.service.PriceService;
import es.bcnc.demo.domain.model.Price;
import es.bcnc.demo.domain.port.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementación del caso de uso {@link PriceService}.
 * <p>
 * Delega en {@link PriceRepositoryPort} para obtener la tarifa
 * aplicable. No contiene ninguna preocupación de infraestructura
 * (caché, mapeo, serialización).
 * </p>
 */
@Service
@RequiredArgsConstructor
public final class PriceServiceImpl implements PriceService {

    /** Puerto de repositorio para la consulta de precios. */
    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price getApplicablePrice(
            final Long brandId,
            final Long productId,
            final LocalDateTime applicationDate) {
        return priceRepositoryPort
                .findApplicablePrice(productId, brandId, applicationDate);
    }
}
