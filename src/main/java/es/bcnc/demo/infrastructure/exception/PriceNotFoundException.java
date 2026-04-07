package es.bcnc.demo.infrastructure.exception;

/**
 * Excepción que se lanza cuando no existe ninguna tarifa
 * aplicable para la combinación de marca, producto y fecha
 * proporcionada.
 * <p>
 * Al ser una {@link RuntimeException}, no obliga a quien la invoca
 * a capturarla explícitamente; su manejo global se delega a
 * {@code GlobalExceptionHandler}.
 * </p>
 */
public class PriceNotFoundException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del motivo.
     *
     * @param message descripción del precio no encontrado
     */
    public PriceNotFoundException(final String message) {
        super(message);
    }
}
