package br.com.fiap.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_CLINICA_ODONTOPREV")
public class ClinicaOdontoprev {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinica_id_seq")
    @SequenceGenerator(name = "clinica_id_seq", sequenceName = "clinica_id_seq", allocationSize = 1)
    @Column(name = "clinica_id", nullable = false)
    private Long clinicaId;

    @Column(name = "nome_clinica", length = 100, nullable = false)
    private String nomeClinica;

    @Column(name = "telefone_clinica", length = 15)
    private String telefoneClinica;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DentistaOdontoprev> dentistas;


    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"clinica"}) // evita o loop
    private List<EnderecoClinicaOdontoprev> enderecos;



}
