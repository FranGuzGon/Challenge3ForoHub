package ForoHub.api.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRespuestaTopico(
                                   Long id,

                                   String titulo,

                                   String mensaje,

                                   Long idUsuario,

                                   String curso) {
}
