package es.bcnc.demo.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI (Swagger).
 * <p>
 * Define los metadatos del API (título, versión, descripción y
 * contacto) que se muestran en la interfaz Swagger UI accesible
 * en {@code /swagger-ui.html}.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Crea y configura el bean {@link OpenAPI} con los metadatos
     * del servicio.
     *
     * @return instancia de {@link OpenAPI} lista para ser usada
     *         por Springdoc
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Price API")
                        .version("1.0")
                        .description(
                                "Servicio de consulta de precios"
                                + " con prioridad de tarifas")
                        .contact(new Contact()
                                .name("Equipo BCNC")
                                .email("demo@example.com")));
    }
}
