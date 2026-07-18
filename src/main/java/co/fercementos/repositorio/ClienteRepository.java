package co.fercementos.repositorio;

import co.fercementos.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio de clientes (CRUD generado por Spring Data JPA). */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
