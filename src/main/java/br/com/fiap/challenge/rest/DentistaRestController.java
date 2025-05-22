package br.com.fiap.challenge.rest;

import br.com.fiap.challenge.model.DentistaOdontoprev;
import br.com.fiap.challenge.service.DentistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentistas")
public class DentistaRestController {

    private final DentistaService dentistaService;

    public DentistaRestController(DentistaService dentistaService) {
        this.dentistaService = dentistaService;
    }

    @GetMapping
    public List<DentistaOdontoprev> listar() {
        return dentistaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistaOdontoprev> buscarPorId(@PathVariable Long id) {
        var dentista = dentistaService.buscarPorId(id);
        return dentista != null ? ResponseEntity.ok(dentista) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DentistaOdontoprev> criar(@RequestBody DentistaOdontoprev dentista) {
        return ResponseEntity.ok(dentistaService.salvar(dentista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistaOdontoprev> atualizar(@PathVariable Long id, @RequestBody DentistaOdontoprev dentista) {
        return ResponseEntity.ok(dentistaService.atualizar(id, dentista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        dentistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
