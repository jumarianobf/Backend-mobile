//package br.com.fiap.challenge.rest;
//
//import br.com.fiap.challenge.model.ImagemUsuarioOdontoprev;
//import br.com.fiap.challenge.service.impl.ImagemUsuarioServiceImpl;
//import br.com.fiap.challenge.service.impl.UsuarioServiceImpl;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/imagens")
//public class ImagemUsuarioRestController {
//
//    private final ImagemUsuarioServiceImpl imagemService;
//    private final UsuarioServiceImpl usuarioService;
//
//    public ImagemUsuarioRestController(ImagemUsuarioServiceImpl imagemService, UsuarioServiceImpl usuarioService) {
//        this.imagemService = imagemService;
//        this.usuarioService = usuarioService;
//    }
//
//    /**
//     * Upload de imagem para um usuário específico
//     */
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImagem(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("usuarioId") Long usuarioId
//    ) {
//        try {
//            String urlImagem = imagemService.salvarArquivo(file);
//            var usuario = usuarioService.buscarPorId(usuarioId);
//
//            var imagem = ImagemUsuarioOdontoprev.builder()
//                    .usuario(usuario)
//                    .imagemUrl(urlImagem)
//                    .dataEnvio(LocalDate.now())
//                    .build();
//
//            var imagemSalva = imagemService.criar(imagem);
//
//            return ResponseEntity.ok(Map.of(
//                    "mensagem", "Upload realizado com sucesso",
//                    "url", imagemSalva.getImagemUrl(),
//                    "imagemId", imagemSalva.getImagemUsuarioId()
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "erro", "Falha no upload: " + e.getMessage()
//            ));
//        }
//    }
//
//    /**
//     * Lista todas as imagens de um usuário
//     */
//    @GetMapping("/usuario/{usuarioId}")
//    public ResponseEntity<List<ImagemUsuarioOdontoprev>> listarPorUsuario(@PathVariable Long usuarioId) {
//        return ResponseEntity.ok(imagemService.listarPorUsuario(usuarioId));
//    }
//}
