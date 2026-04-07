package es.bcnc.demo.infrastructure.exception;

/**
 * Excepción lanzada cuando no existe ninguna marca para el
 * identificador proporcionado.
 * Su manejo global se delega a {@code GlobalExceptionHandler}.
 */
public class BrandNotFoundException extends RuntimeException {

    /**
     * Construye la excepción con el identificador de marca no
     * encontrado.
     *
     * @param id identificador de la marca que no existe
     */
    public BrandNotFoundException(final Long id) {
        super("No brand found for id=" + id);
    }
}
