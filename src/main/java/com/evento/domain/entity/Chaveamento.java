package com.evento.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "chaveamentos")
public class Chaveamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fase;

    private int ordem;

    @JsonIgnoreProperties({"hoteis", "aeroportos", "descricao", "imagemUrl"})
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partida_id")
    private Partida partida;

    public Chaveamento() {}

    public Chaveamento(String fase, int ordem, Partida partida) {
        this.fase = fase;
        this.ordem = ordem;
        this.partida = partida;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }
}
