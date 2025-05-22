package br.com.fiap.challenge.service.impl;

import br.com.fiap.challenge.config.RabbitConfig;
import br.com.fiap.challenge.messaging.ImagemUsuarioEvent;
import br.com.fiap.challenge.model.ImagemUsuarioOdontoprev;
import br.com.fiap.challenge.repository.ImagemUsuarioRepository;
import br.com.fiap.challenge.service.ImagemUsuarioService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class ImagemUsuarioServiceImpl implements ImagemUsuarioService {

    private final ImagemUsuarioRepository repo;
    private final RabbitTemplate rabbitTemplate;
    private final UsuarioServiceImpl usuarioService;

    public ImagemUsuarioServiceImpl(
            ImagemUsuarioRepository repo,
            RabbitTemplate rabbitTemplate,
            UsuarioServiceImpl usuarioService
    ) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
        this.usuarioService = usuarioService;
    }

    private void publishEvent(ImagemUsuarioEvent.Tipo tipo,
                              ImagemUsuarioOdontoprev img) {
        var evt = new ImagemUsuarioEvent(
                tipo,
                img.getImagemUsuarioId(),
                img.getUsuario().getUsuarioId(),
                img.getImagemUrl(),
                img.getDataEnvio()
        );
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_IMAGENS_USUARIO,
                RabbitConfig.ROUTING_KEY_IMAGEM_USUARIO,
                evt
        );
    }

    @Override
    public ImagemUsuarioOdontoprev criar(ImagemUsuarioOdontoprev imagem) {
        var salvo = repo.save(imagem);
        publishEvent(ImagemUsuarioEvent.Tipo.CRIADO, salvo);
        return salvo;
    }

    @Override
    public List<ImagemUsuarioOdontoprev> listarTodos() {
        return repo.findAll();
    }

    @Override
    public ImagemUsuarioOdontoprev buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));
    }

    @Override
    public ImagemUsuarioOdontoprev atualizar(Long id, ImagemUsuarioOdontoprev upd) {
        var existente = buscarPorId(id);
        existente.setImagemUrl(upd.getImagemUrl());
        existente.setDataEnvio(upd.getDataEnvio());
        existente.setUsuario(upd.getUsuario());
        var atualizado = repo.save(existente);
        publishEvent(ImagemUsuarioEvent.Tipo.ATUALIZADO, atualizado);
        return atualizado;
    }

    @Override
    public void deletar(Long id) {
        var exist = buscarPorId(id);
        repo.deleteById(id);
        publishEvent(ImagemUsuarioEvent.Tipo.DELETADO, exist);
    }


    /**
     * 🔥 Salvar o arquivo fisicamente e criar o registro no banco.
     */
    @Override
    public ImagemUsuarioOdontoprev salvarArquivo(MultipartFile file, Long usuarioId) {
        try {
            // 🔹 Salvar arquivo no servidor
            String pastaUpload = "uploads/";
            File diretorio = new File(pastaUpload);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(pastaUpload + nomeArquivo);
            Files.write(caminho, file.getBytes());

            String url = "/uploads/" + nomeArquivo;

            // 🔹 Criar entidade no banco
            var usuario = usuarioService.buscarPorId(usuarioId);

            ImagemUsuarioOdontoprev imagem = ImagemUsuarioOdontoprev.builder()
                    .usuario(usuario)
                    .imagemUrl(url)
                    .dataEnvio(LocalDate.now())
                    .build();

            var salvo = repo.save(imagem);
            publishEvent(ImagemUsuarioEvent.Tipo.CRIADO, salvo);

            return salvo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @Override
    public List<ImagemUsuarioOdontoprev> listarPorUsuario(Long usuarioId) {
        var usuario = usuarioService.buscarPorId(usuarioId);
        return repo.findByUsuario(usuario);
    }

    /**
     * 🔹 Apenas salva o arquivo no servidor e retorna a URL (sem salvar no banco).
     */
    public String uploadArquivoSimples(MultipartFile file) {
        try {
            String pastaUpload = "uploads/";
            File diretorio = new File(pastaUpload);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(pastaUpload + nomeArquivo);
            Files.write(caminho, file.getBytes());

            return "/uploads/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}
