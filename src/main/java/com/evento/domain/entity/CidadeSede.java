package com.evento.domain.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cidades_sede")
public class CidadeSede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String pais;

    @Column(length = 1000)
    private String descricao;

    private String imagemUrl;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;

    @OneToMany(mappedBy = "cidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Hotel> hoteis = new ArrayList<>();

    @OneToMany(mappedBy = "cidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aeroporto> aeroportos = new ArrayList<>();

    public CidadeSede() {}

    public CidadeSede(String nome, String pais, String descricao) {
        this.nome = nome;
        this.pais = pais;
        this.descricao = descricao;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Estadio getEstadio() { return estadio; }
    public void setEstadio(Estadio estadio) { this.estadio = estadio; }

    public List<Hotel> getHoteis() { return hoteis; }
    public void setHoteis(List<Hotel> hoteis) { this.hoteis = hoteis; }

    public List<Aeroporto> getAeroportos() { return aeroportos; }
    public void setAeroportos(List<Aeroporto> aeroportos) { this.aeroportos = aeroportos; }
}
