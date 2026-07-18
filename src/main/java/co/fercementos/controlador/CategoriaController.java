package co.fercementos.controlador;

import co.fercementos.modelo.Categoria;
import co.fercementos.repositorio.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD de categorias.
 * Convencion de rutas usada en todos los modulos:
 *   GET  /categorias                -> listado
 *   GET  /categorias/nueva          -> formulario vacio
 *   GET  /categorias/editar/{id}    -> formulario precargado
 *   POST /categorias/guardar        -> insertar o actualizar
 *   GET  /categorias/eliminar/{id}  -> eliminar
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada: " + id));
        model.addAttribute("categoria", categoria);
        return "categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "categorias/formulario";
        }
        // save() inserta si el id es null y actualiza si ya existe
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}
