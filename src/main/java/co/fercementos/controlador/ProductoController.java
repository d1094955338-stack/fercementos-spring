package co.fercementos.controlador;

import co.fercementos.modelo.Categoria;
import co.fercementos.modelo.Producto;
import co.fercementos.modelo.Proveedor;
import co.fercementos.repositorio.CategoriaRepository;
import co.fercementos.repositorio.ProductoRepository;
import co.fercementos.repositorio.ProveedorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD de productos.
 *
 * El formulario incluye dos listas desplegables (categoria y proveedor)
 * pobladas desde la base de datos, reflejando las relaciones @ManyToOne
 * del modelo de dominio y las FK del script SQL oficial. Ademas soporta
 * busqueda por nombre mediante el parametro opcional "q".
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoController(ProductoRepository productoRepository,
                              CategoriaRepository categoriaRepository,
                              ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @GetMapping
    public String listar(@RequestParam(name = "q", required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("productos", productoRepository.findByNombreContainingIgnoreCase(q));
            model.addAttribute("q", q);
        } else {
            model.addAttribute("productos", productoRepository.findAll());
        }
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Producto producto = new Producto();
        prepararRelaciones(producto);   // evita NullPointer al enlazar el formulario
        model.addAttribute("producto", producto);
        cargarListas(model);
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        prepararRelaciones(producto);
        model.addAttribute("producto", producto);
        cargarListas(model);
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            cargarListas(model);
            return "productos/formulario";
        }
        normalizarRelaciones(producto); // "-- Sin categoria --" => relacion en null
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }

    /** Carga las listas desplegables del formulario (categorias y proveedores). */
    private void cargarListas(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
    }

    /**
     * Garantiza que categoria y proveedor no sean null al renderizar el
     * formulario: Thymeleaf necesita navegar *{categoria.idCategoria}.
     */
    private void prepararRelaciones(Producto producto) {
        if (producto.getCategoria() == null) {
            producto.setCategoria(new Categoria());
        }
        if (producto.getProveedor() == null) {
            producto.setProveedor(new Proveedor());
        }
    }

    /**
     * Si el usuario eligio "-- Sin categoria/proveedor --", el objeto llega
     * con id null; se pone la relacion completa en null para que Hibernate
     * guarde la FK como NULL y no intente crear registros vacios.
     */
    private void normalizarRelaciones(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() == null) {
            producto.setCategoria(null);
        }
        if (producto.getProveedor() != null && producto.getProveedor().getIdProveedor() == null) {
            producto.setProveedor(null);
        }
    }
}
