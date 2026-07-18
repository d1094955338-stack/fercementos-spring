package co.fercementos.repositorio;

import co.fercementos.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de categorias. Al extender JpaRepository, Spring Data JPA
 * genera automaticamente las operaciones CRUD (findAll, findById, save,
 * deleteById...), lo que en la version con Servlets requeria escribir
 * cada consulta SQL a mano en el DAO.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
