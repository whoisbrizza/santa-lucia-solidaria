
package br.com.whoisbrizza.santaluciasolidaria.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "caronas")
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String origem;

    @Column(nullable = false, length = 150)
    private String destino;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private Integer vagasDisponiveis;

    @ManyToOne
    @JoinColumn(name = "motorista_id", nullable = false)
    private Usuario motorista;

    public Carona() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Integer getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(Integer vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public Usuario getMotorista() {
        return motorista;
    }

    public void setMotorista(Usuario motorista) {
        this.motorista = motorista;
    }
}
