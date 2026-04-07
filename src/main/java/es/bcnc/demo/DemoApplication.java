package es.bcnc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
@EnableCaching
public final class DemoApplication {

    private DemoApplication() {
    }

    /**
     * Método principal que arranca el contexto de Spring Boot.
     *
     * @param args argumentos de línea de comandos (no requeridos)
     */
    public static void main(final String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
