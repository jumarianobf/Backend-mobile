package br.com.fiap.challenge.rest;

import br.com.fiap.challenge.model.AtendimentoUsuarioOdontoprev;
import br.com.fiap.challenge.service.impl.AtendimentoUsuarioServiceImpl;
import br.com.fiap.challenge.service.ImagemUsuarioService;
import br.com.fiap.challenge.service.impl.ImagemUsuarioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AtendimentoUsuarioRestController {

    private final AtendimentoUsuarioServiceImpl atendimentoUsuarioService;
    private final ImagemUsuarioServiceImpl imagemUsuarioService; // <-- Adiciona isso

    // 🔹 Listar todos
    @GetMapping
    public ResponseEntity<List<AtendimentoUsuarioOdontoprev>> listarTodos() {
        return ResponseEntity.ok(atendimentoUsuarioService.listarTodos());
    }

    // 🔹 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoUsuarioOdontoprev> buscarPorId(@PathVariable Long id) {
        var atendimento = atendimentoUsuarioService.buscarPorId(id);
        return ResponseEntity.ok(atendimento);
    }

    // 🔹 Listar por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AtendimentoUsuarioOdontoprev>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(atendimentoUsuarioService.listarPorUsuario(usuarioId));
    }

    // 🔹 Criar novo atendimento simples (sem imagem)
    @PostMapping
    public ResponseEntity<AtendimentoUsuarioOdontoprev> criar(@RequestBody AtendimentoUsuarioOdontoprev atendimento) {
        var novo = atendimentoUsuarioService.criar(atendimento);
        return ResponseEntity.ok(novo);
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<AtendimentoUsuarioOdontoprev> criarComImagem(
            @RequestPart("atendimento") String atendimentoJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // ✅ ISSO É OBRIGATÓRIO

            AtendimentoUsuarioOdontoprev atendimento = mapper
                    .readValue(atendimentoJson, AtendimentoUsuarioOdontoprev.class);

            // 🔹 Resto do código para salvar imagem e atendimento
            return ResponseEntity.ok(atendimentoUsuarioService.criar(atendimento));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // 🔹 Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoUsuarioOdontoprev> atualizar(
            @PathVariable Long id,
            @RequestBody AtendimentoUsuarioOdontoprev atendimentoAtualizado) {
        var atualizado = atendimentoUsuarioService.atualizar(id, atendimentoAtualizado);
        return ResponseEntity.ok(atualizado);
    }

    // 🔹 Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atendimentoUsuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
