package br.com.fiap.challenge.model;

import br.com.fiap.challenge.service.impl.ImagemUsuarioServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "T_ATENDIMENTO_USUARIO_ODONTOPREV")
public class AtendimentoUsuarioOdontoprev {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atendimento_id_seq")
    @SequenceGenerator(name = "atendimento_id_seq", sequenceName = "atendimento_id_seq", allocationSize = 1)
    @Column(name = "atendimento_id")
    private Long atendimentoId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioOdontoprev usuario;

    @ManyToOne
    @JoinColumn(name = "dentista_id", nullable = false)
    private DentistaOdontoprev dentista;

    @ManyToOne
    @JoinColumn(name = "clinica_id", nullable = false)
    private ClinicaOdontoprev clinica;

    @Column(name = "data_atendimento", nullable = false)
    private LocalDate dataAtendimento;

    @Column(name = "descricao_procedimento", length = 100)
    private String descricaoProcedimento;

    @Column(name = "custo", precision = 10, scale = 2)
    private BigDecimal custo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "data_registro", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate dataRegistro;

    @ManyToOne
    @JoinColumn(name = "imagem_usuario_id", nullable = true)
    private ImagemUsuarioOdontoprev imagemUrl;

    @Column(name = "status", length = 20)
    private String status;
}



