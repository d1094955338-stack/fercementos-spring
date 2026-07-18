package co.fercementos.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA mapeada a la tabla "detalle_venta" del script SQL oficial.
 */
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    /** Muchas lineas de detalle pertenecen a una venta (FK id_venta). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    private Venta venta;

    /** Cada linea referencia un producto vendido (FK id_producto). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;

    /**
     * Precio unitario al momento de la venta. Se copia desde el producto
     * al registrar la linea, para que el historial no cambie si el precio
     * del producto se actualiza despues.
     */
    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    public DetalleVenta() {
    }

    /** Subtotal de la linea = cantidad x precio unitario. */
    @Transient
    public BigDecimal getSubtotal() {
        if (cantidad == null || precio == null) {
            return BigDecimal.ZERO;
        }
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
