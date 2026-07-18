package co.fercementos.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA mapeada a la tabla "ventas" del script SQL oficial.
 *
 * La tabla no tiene columna "total": el total se calcula sumando los
 * subtotales de las lineas de detalle (metodo getTotal()).
 */
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha")
    private LocalDate fecha;

    /** Muchas ventas pertenecen a un cliente (FK id_cliente). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    /**
     * Lineas de detalle de la venta. orphanRemoval + cascade permiten
     * que al guardar/eliminar la venta se gestionen tambien sus detalles.
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
    }

    /** Total de la venta = suma de (cantidad x precio) de cada detalle. */
    @Transient
    public BigDecimal getTotal() {
        return detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getIdVenta() { return idVenta; }
    public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
