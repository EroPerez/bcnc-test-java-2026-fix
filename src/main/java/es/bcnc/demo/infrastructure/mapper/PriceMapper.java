package es.bcnc.demo.infrastructure.mapper;

import es.bcnc.demo.domain.model.Price;
import es.bcnc.demo.infrastructure.entity.PriceEntity;
import es.bcnc.demo.infrastructure.rest.dto.PriceResponse;

import org.springframework.stereotype.Component;

/**
 * Componente de mapeo de infraestructura.
 * <ul>
 *   <li>{@code PriceEntity} → {@link Price}
 *       (persistencia → dominio)</li>
 *   <li>{@link Price} → {@link PriceResponse}
 *       (dominio → DTO HTTP)</li>
 * </ul>
 */
@Component
public final class PriceMapper {

    /**
     * Convierte una entidad JPA al modelo de dominio.
     *
     * @param entity entidad JPA a convertir
     * @return modelo de dominio, o {@code null} si la entidad es nula
     */
    public Price toDomain(final PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Price.builder()
                .brandId(entity.getBrand().getId())
                .brandName(entity.getBrand().getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .build();
    }

    /**
     * Convierte un modelo de dominio al DTO de respuesta HTTP.
     *
     * @param domain modelo de dominio a convertir
     * @return DTO de respuesta, o {@code null} si el dominio es nulo
     */
    public PriceResponse toResponse(final Price domain) {
        if (domain == null) {
            return null;
        }

        return PriceResponse.builder()
                .productId(domain.getProductId())
                .brandId(domain.getBrandId())
                .brandName(domain.getBrandName())
                .priceList(domain.getPriceList())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .price(domain.getPrice())
                .build();
    }
}
