package ForoHub.api.domain.topico;

import ForoHub.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name ="topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario usuario;

    @Column(name = "fecha_creacion", updatable = false, insertable = false )
    private LocalDateTime fechaCreacion;

    private String curso;
    private boolean status = true;// true = activo, false = inactivo

    public Topico(DatosRegistroTopico datosRegistroTopico, Usuario usuario) {
        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.usuario = usuario;
        this.curso = datosRegistroTopico.curso();
        this.status = true;
    }

    public boolean isActive() {
        return status;
    }

    public void setActive(boolean active){
        this.status = active;
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico, Usuario usuario) {
        if(datosActualizarTopico.titulo() != null) {
          this.titulo = datosActualizarTopico.titulo();
        }
        if (datosActualizarTopico.mensaje() != null) {
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if (datosActualizarTopico.curso() != null) {
            this.curso = datosActualizarTopico.curso();
        }
        if (usuario != null){
            this.usuario = usuario;
        }

    }
}
