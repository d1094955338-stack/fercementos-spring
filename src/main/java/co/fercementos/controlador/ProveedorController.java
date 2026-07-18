package co.fercementos.controlador;

import co.fercementos.modelo.Proveedor;
import co.fercementos.repositorio.ProveedorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** CRUD de proveedores (misma convencion de rutas que /categorias). */
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;

    public ProveedorController(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));
        model.addAttribute("proveedor", proveedor);
        return "proveedores/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                          BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "proveedores/formulario";
        }
        proveedorRepository.save(proveedor);
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        proveedorRepository.deleteById(id);
        return "redirect:/proveedores";
    }
}
