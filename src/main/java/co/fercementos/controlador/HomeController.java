package co.fercementos.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Menu principal de la aplicacion (requiere sesion iniciada). */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}
