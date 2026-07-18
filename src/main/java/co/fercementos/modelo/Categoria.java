package co.fercementos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA mapeada a la tabla "categorias" del script SQL oficial
 * (Evidencia GA6-220501096-AA2-EV03).
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Column(name = "nombre_categoria", nullable = false, length = 100)
    private String nombreCategoria;

    public Categoria() {
    }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}
