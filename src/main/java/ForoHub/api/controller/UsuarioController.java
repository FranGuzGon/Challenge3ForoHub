package ForoHub.api.controller;


import ForoHub.api.domain.usuario.DatosCreacionUsuario;
import ForoHub.api.domain.usuario.DatosRegistroUsuario;
import ForoHub.api.domain.usuario.Usuario;
import ForoHub.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosCreacionUsuario> registrarUsuarios(@RequestBody DatosRegistroUsuario datosRegistroUsuario,
                                            UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario));
        DatosCreacionUsuario datosCreacionUsuario = new DatosCreacionUsuario(usuario.getId(), usuario.getNombre(), usuario.getMail());
        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosCreacionUsuario);
    }
}
