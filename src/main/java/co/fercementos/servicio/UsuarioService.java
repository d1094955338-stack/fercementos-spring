package co.fercementos.servicio;

import co.fercementos.modelo.Usuario;
import co.fercementos.repositorio.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de usuarios. Cumple dos funciones:
 *
 * 1. Implementa UserDetailsService: es la pieza que Spring Security
 *    invoca durante el login para cargar el usuario desde la tabla
 *    "usuarios" y comparar la contrasena (pantalla "Iniciar sesion"
 *    del prototipo, y validacion "Usuario valido?" del diagrama de
 *    funcionalidades).
 *
 * 2. Expone el metodo registrar() para la pantalla "Crear cuenta",
 *    aplicando hash BCrypt a la contrasena antes de guardarla.
 */
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /** Inyeccion de dependencias por constructor (practica recomendada). */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe el usuario: " + nombreUsuario));

        // Se adapta nuestra entidad Usuario al modelo de Spring Security.
        return User.withUsername(usuario.getNombreUsuario())
                .password(usuario.getContrasena())   // hash BCrypt guardado en BD
                .roles("USER")
                .build();
    }

    /**
     * Registra un usuario nuevo, aplicando hash BCrypt a la contrasena.
     * @return false si el nombre de usuario ya existe.
     */
    public boolean registrar(Usuario usuario) {
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return false;
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return true;
    }
}
