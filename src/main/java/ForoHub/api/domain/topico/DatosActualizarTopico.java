package ForoHub.api.domain.topico;

import ForoHub.api.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(

        @NotNull
        Long id,

        @NotBlank
        String titulo,

        @NotBlank
        String mensaje,

        @NotNull
        Long idUsuario,

        @NotBlank
        String curso
) {
}
