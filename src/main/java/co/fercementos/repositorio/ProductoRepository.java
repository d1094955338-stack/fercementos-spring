package co.fercementos.repositorio;

import co.fercementos.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio de productos. Ademas del CRUD generado, se declara una
 * consulta derivada: Spring construye el SQL a partir del nombre del
 * metodo (busqueda por nombre, ignorando mayusculas/minusculas).
 */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
