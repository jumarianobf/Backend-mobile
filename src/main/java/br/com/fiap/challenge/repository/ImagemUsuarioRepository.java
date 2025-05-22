package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.ImagemUsuarioOdontoprev;
import br.com.fiap.challenge.model.UsuarioOdontoprev;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemUsuarioRepository extends JpaRepository<ImagemUsuarioOdontoprev, Long> {
    List<ImagemUsuarioOdontoprev> findByUsuario(UsuarioOdontoprev usuario);
}
