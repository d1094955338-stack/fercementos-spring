package co.fercementos.repositorio;

import co.fercementos.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de usuarios. La consulta derivada findByNombreUsuario se
 * usa tanto para el login (Spring Security) como para validar duplicados
 * en el registro.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuario(String nombreUsuario);
}
