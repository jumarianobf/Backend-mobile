package br.com.fiap.challenge.rest;

import br.com.fiap.challenge.model.UsuarioOdontoprev;
import br.com.fiap.challenge.service.impl.UsuarioServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioServiceImpl usuarioService;

    public UsuarioRestController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioOdontoprev> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOdontoprev> buscarPorId(@PathVariable Long id) {
        var usuario = usuarioService.buscarPorId(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UsuarioOdontoprev> criar(@RequestBody UsuarioOdontoprev usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioOdontoprev> atualizar(@PathVariable Long id, @RequestBody UsuarioOdontoprev usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioOdontoprev> buscarPorCpf(@PathVariable String cpf) {
        var usuario = usuarioService.buscarPorCpf(cpf);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

}
