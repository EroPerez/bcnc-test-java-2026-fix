package es.bcnc.demo.application.service;

import es.bcnc.demo.domain.model.Price;

import java.time.LocalDateTime;

/**
 * Interfaz de servicio para operaciones relacionadas con precios.
 */
public interface PriceService {

    /**
     * Devuelve el precio aplicable para un producto, marca y fecha dados.
     * <p>
     * Cuando múltiples entradas de precio se solapan para el mismo
     * producto y marca, se devuelve la de mayor prioridad.
     *
     * @param brandId         identificador de la marca (p. ej. 1 = ZARA)
     * @param productId       identificador del producto
     * @param applicationDate fecha y hora para la que se consulta el precio
     * @return el {@link Price} aplicable con la lista de precios,
     *         prioridad, rango de validez y precio final
     */
    Price getApplicablePrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate);
}
