package br.com.fiap.challenge.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String pastaUpload = "uploads/";

    public String salvarArquivo(MultipartFile file) {
        try {
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
