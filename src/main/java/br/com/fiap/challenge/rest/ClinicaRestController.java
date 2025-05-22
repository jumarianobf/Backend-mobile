package br.com.fiap.challenge.rest;

import br.com.fiap.challenge.model.ClinicaOdontoprev;
import br.com.fiap.challenge.service.impl.ClinicaServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinicas")
public class ClinicaRestController {

    private final ClinicaServiceImpl clinicaService;

    public ClinicaRestController(ClinicaServiceImpl clinicaService) {
        this.clinicaService = clinicaService;
    }

    @GetMapping
    public List<ClinicaOdontoprev> listar() {
        return clinicaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicaOdontoprev> buscarPorId(@PathVariable Long id) {
        var clinica = clinicaService.buscarPorId(id);
        return clinica != null ? ResponseEntity.ok(clinica) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ClinicaOdontoprev> criar(@RequestBody ClinicaOdontoprev clinica) {
        return ResponseEntity.ok(clinicaService.salvar(clinica));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicaOdontoprev> atualizar(@PathVariable Long id, @RequestBody ClinicaOdontoprev clinica) {
        return ResponseEntity.ok(clinicaService.atualizar(id, clinica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clinicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
