package co.fercementos.controlador;

import co.fercementos.modelo.Usuario;
import co.fercementos.servicio.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador de autenticacion.
 *
 * El POST del login NO esta aqui: lo procesa Spring Security
 * (ver SecurityConfig). Este controlador solo muestra la pagina de
 * login y gestiona el registro de cuentas nuevas (pantallas "Iniciar
 * sesion" y "Crear cuenta" del prototipo Balsamiq).
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** GET /login: muestra el formulario de inicio de sesion. */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** GET /registro: muestra el formulario de creacion de cuenta. */
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    /**
     * POST /registro: valida y crea el usuario.
     * @Valid dispara las validaciones declaradas en la entidad Usuario;
     * los errores llegan en BindingResult y se muestran en la vista.
     */
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult resultado, Model model) {

        if (resultado.hasErrors()) {
            return "registro";
        }

        boolean creado = usuarioService.registrar(usuario);

        if (!creado) {
            // Mensaje definido en el Caso de Uso 001 (excepcion 3)
            model.addAttribute("error", "El usuario ya se encuentra registrado en la base de datos.");
            return "registro";
        }

        return "redirect:/login?cuentaCreada=true";
    }
}
