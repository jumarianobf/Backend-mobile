package br.com.fiap.challenge.service;

import br.com.fiap.challenge.model.ClinicaOdontoprev;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ClinicaService {
    ClinicaOdontoprev criar(ClinicaOdontoprev clinica);
    List<ClinicaOdontoprev> listarTodas();
    @Transactional
    ClinicaOdontoprev buscarPorId(Long id);
    ClinicaOdontoprev atualizar(Long id, ClinicaOdontoprev clinicaAtualizada);
    void deletar(Long id);
    ClinicaOdontoprev salvar(ClinicaOdontoprev clinica);
}
