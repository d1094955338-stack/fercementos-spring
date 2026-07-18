package co.fercementos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * La anotacion @SpringBootApplication activa la autoconfiguracion del
 * framework y el escaneo de componentes (@Controller, @Service,
 * @Repository, @Entity) dentro del paquete co.fercementos.
 */
@SpringBootApplication
public class FercementosApplication {

    public static void main(String[] args) {
        SpringApplication.run(FercementosApplication.class, args);
    }
}
