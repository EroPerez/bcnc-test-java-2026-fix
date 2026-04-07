package es.bcnc.demo.domain.port;

import es.bcnc.demo.domain.model.Price;

import java.time.LocalDateTime;

/**
 * Puerto de salida (outbound port) del dominio para la consulta
 * de precios.
 * <p>
 * Define el contrato que debe implementar cualquier adaptador de
 * persistencia. Siguiendo el patrón de arquitectura hexagonal, el
 * dominio no conoce los detalles de la tecnología de almacenamiento
 * subyacente.
 * </p>
 */
public interface PriceRepositoryPort {

    /**
     * Busca la tarifa de precio vigente para un producto y marca
     * en una fecha concreta.
     * <p>
     * Cuando varias tarifas se solapan, se espera que el adaptador
     * devuelva la de mayor prioridad.
     * </p>
     *
     * @param productId       identificador del producto
     * @param brandId         identificador de la marca
     * @param applicationDate fecha y hora de consulta
     * @return la tarifa aplicable, o lanza una excepción si no existe
     */
    Price findApplicablePrice(
            Long productId,
            Long brandId,
            LocalDateTime applicationDate);
}
