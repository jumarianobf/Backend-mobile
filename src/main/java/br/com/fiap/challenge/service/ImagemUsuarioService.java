package br.com.fiap.challenge.service;

import br.com.fiap.challenge.model.ImagemUsuarioOdontoprev;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImagemUsuarioService {
    ImagemUsuarioOdontoprev criar(ImagemUsuarioOdontoprev imagem);
    List<ImagemUsuarioOdontoprev> listarTodos();
    ImagemUsuarioOdontoprev buscarPorId(Long id);
    ImagemUsuarioOdontoprev atualizar(Long id, ImagemUsuarioOdontoprev imagemAtualizada);
    void deletar(Long id);

    // 🔥 Método correto para salvar a imagem no disco e no banco
    ImagemUsuarioOdontoprev salvarArquivo(MultipartFile file, Long usuarioId);

    // 🔥 Buscar todas as imagens de um usuário específico
    List<ImagemUsuarioOdontoprev> listarPorUsuario(Long usuarioId);

    String uploadArquivoSimples(MultipartFile file);

}
