package co.fercementos.controlador;

import co.fercementos.modelo.DetalleVenta;
import co.fercementos.modelo.Producto;
import co.fercementos.modelo.Venta;
import co.fercementos.repositorio.ClienteRepository;
import co.fercementos.repositorio.ProductoRepository;
import co.fercementos.repositorio.VentaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modulo de ventas.
 *
 * Flujo (segun el diagrama de modelo de dominio: la venta actualiza el
 * inventario):
 *   1. Se crea la venta (cliente + fecha).
 *   2. En la pantalla de detalle se agregan lineas (producto + cantidad).
 *      El precio unitario se copia del producto y el stock se descuenta.
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public VentaController(VentaRepository ventaRepository,
                           ClienteRepository clienteRepository,
                           ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaRepository.findAll());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Venta venta = new Venta();
        venta.setFecha(LocalDate.now());
        // Se inicializa el cliente vacio para que Thymeleaf pueda enlazar
        // el campo *{cliente.idCliente} sin NullPointerException.
        venta.setCliente(new co.fercementos.modelo.Cliente());
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteRepository.findAll());
        return "ventas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("venta") Venta venta) {
        ventaRepository.save(venta);
        // Tras crear la cabecera se pasa a la pantalla de detalle
        return "redirect:/ventas/detalle/" + venta.getIdVenta();
    }

    /** Pantalla de detalle: lineas de la venta + formulario para agregar. */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));
        model.addAttribute("venta", venta);
        model.addAttribute("productos", productoRepository.findAll());
        return "ventas/detalle";
    }

    /**
     * Agrega una linea de detalle a la venta y descuenta el stock del
     * producto. @Transactional garantiza que ambas operaciones (guardar
     * detalle y actualizar stock) se confirmen juntas o ninguna.
     */
    @PostMapping("/detalle/{id}/agregar")
    @Transactional
    public String agregarDetalle(@PathVariable Integer id,
                                 @RequestParam Integer idProducto,
                                 @RequestParam Integer cantidad,
                                 Model model) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + id));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));

        // Validacion de stock disponible antes de vender
        if (cantidad == null || cantidad <= 0 || producto.getStock() == null
                || producto.getStock() < cantidad) {
            model.addAttribute("venta", venta);
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("error", "Cantidad invalida o stock insuficiente para "
                    + producto.getNombre() + " (disponible: " + producto.getStock() + ").");
            return "ventas/detalle";
        }

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        // El precio se copia del producto para conservar el historial
        detalle.setPrecio(producto.getPrecio() != null ? producto.getPrecio() : BigDecimal.ZERO);

        venta.getDetalles().add(detalle);

        // El inventario se actualiza con la venta (modelo de dominio)
        producto.setStock(producto.getStock() - cantidad);

        ventaRepository.save(venta);
        productoRepository.save(producto);

        return "redirect:/ventas/detalle/" + id;
    }

    /** Elimina la venta y devuelve el stock de sus lineas al inventario. */
    @GetMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Integer id) {
        ventaRepository.findById(id).ifPresent(venta -> {
            for (DetalleVenta d : venta.getDetalles()) {
                Producto p = d.getProducto();
                if (p != null && p.getStock() != null && d.getCantidad() != null) {
                    p.setStock(p.getStock() + d.getCantidad());
                    productoRepository.save(p);
                }
            }
            ventaRepository.delete(venta);
        });
        return "redirect:/ventas";
    }
}
