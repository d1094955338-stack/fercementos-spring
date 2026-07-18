package co.fercementos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Usuario del sistema. Campos definidos a partir del prototipo Balsamiq
 * "prototipo_inicio_sesion_de_usuarios.bmpr" (pantalla "Crear cuenta"):
 * nombre de usuario, contrasena, correo, numero de identificacion y
 * fecha de nacimiento.
 *
 * La contrasena se guarda con hash BCrypt (ver UsuarioService), nunca
 * en texto plano.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50)
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @NotBlank(message = "La contrasena es obligatoria")
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Email(message = "El correo no tiene un formato valido")
    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "documento", length = 20)
    private String documento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    public Usuario() {
    }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
