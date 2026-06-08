package com.evento.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "aeroportos")
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 3)
    private String codigo; // IATA code

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_id")
    private CidadeSede cidade;

    public Aeroporto() {}

    public Aeroporto(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public CidadeSede getCidade() { return cidade; }
    public void setCidade(CidadeSede cidade) { this.cidade = cidade; }
}
