package es.bcnc.demo.domain.port;

import es.bcnc.demo.domain.model.Brand;

/**
 * Puerto del dominio para el acceso a la información de marcas.
 */
public interface BrandRepositoryPort {

    /**
     * Busca una marca por su identificador.
     *
     * @param id identificador de la cadena/marca
     * @return la marca encontrada, o lanza una excepción si no existe
     */
    Brand findById(Long id);
}
