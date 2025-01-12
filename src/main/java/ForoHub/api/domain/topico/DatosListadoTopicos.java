package ForoHub.api.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopicos(

        Long id,
        String titulo,
        String mensaje,
        String fechaCreacion,
        String estado,
        String nombreAutor,
        String curso
) {
    public DatosListadoTopicos(Topico topico){
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion() !=null ? topico.getFechaCreacion().toString() : null,
                topico.isActive() ? "Activo" : "Inactivo", //Conversión de boolean a String
                topico.getUsuario().getNombre(),
                topico.getCurso());


    }
}
