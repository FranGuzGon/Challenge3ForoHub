package ForoHub.api.controller;

import ForoHub.api.domain.topico.*;
import ForoHub.api.domain.usuario.Usuario;
import ForoHub.api.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder) {

        //Validar si existe un tópico con el mismo título y mensaje
        boolean topicoExiste = topicoRepository.findByTituloAndMensaje(
                datosRegistroTopico.titulo(),
                datosRegistroTopico.mensaje()
        ).isPresent();

        if (topicoExiste) {
            throw new RuntimeException("Ya existe un topico con el mismo título y mensaje");
        }

        Usuario usuario = usuarioRepository.findById(datosRegistroTopico.idUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Topico topico = new Topico(datosRegistroTopico, usuario);
        topicoRepository.save(topico);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getUsuario().getId(), topico.getCurso());
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion)
                .map(DatosListadoTopicos::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){

        //Validar si existe un tópico con el mismo título y mensaje
        boolean topicoExiste = topicoRepository.findByTituloAndMensaje(
                datosActualizarTopico.titulo(),
                datosActualizarTopico.mensaje()
        ).isPresent();

        if (topicoExiste) {
            throw new RuntimeException("Ya existe un topico con el mismo título y mensaje");
        }
        Topico topico = topicoRepository.getReferenceById(datosActualizarTopico.id());
        Usuario usuario = usuarioRepository.getReferenceById(datosActualizarTopico.idUsuario());
        topico.actualizarDatos(datosActualizarTopico, usuario);
        topicoRepository.save(topico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getUsuario().getId(),
                topico.getCurso()));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornaDatosTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getUsuario().getId(), topico.getCurso());
        return ResponseEntity.ok(datosTopico);
    }
}


