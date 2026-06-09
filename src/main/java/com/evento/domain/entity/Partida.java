package com.evento.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long apiId;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time1_id", nullable = false)
    private Selecao time1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time2_id", nullable = false)
    private Selecao time2;

    private Integer golsTime1;
    private Integer golsTime2;

    @Column(nullable = false)
    private String fase;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;

    // Serializa apenas os campos necessários — evita loop CidadeSede → hoteis/aeroportos → CidadeSede
    @JsonIgnoreProperties({"hoteis", "aeroportos", "descricao", "imagemUrl"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cidade_id")
    private CidadeSede cidade;

    public Partida() {}

    public Partida(LocalDate data, LocalTime horario, Selecao time1, Selecao time2,
                   String fase, String status, Estadio estadio, CidadeSede cidade) {
        this.data = data;
        this.horario = horario;
        this.time1 = time1;
        this.time2 = time2;
        this.fase = fase;
        this.status = status;
        this.estadio = estadio;
        this.cidade = cidade;
    }

    // Helper
    public String getPlacar() {
        if (golsTime1 == null || golsTime2 == null) return "- x -";
        return golsTime1 + " x " + golsTime2;
    }

    public boolean isFinalizada() {
        return "FINALIZADA".equals(status);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApiId() { return apiId; }
    public void setApiId(Long apiId) { this.apiId = apiId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public Selecao getTime1() { return time1; }
    public void setTime1(Selecao time1) { this.time1 = time1; }

    public Selecao getTime2() { return time2; }
    public void setTime2(Selecao time2) { this.time2 = time2; }

    public Integer getGolsTime1() { return golsTime1; }
    public void setGolsTime1(Integer golsTime1) { this.golsTime1 = golsTime1; }

    public Integer getGolsTime2() { return golsTime2; }
    public void setGolsTime2(Integer golsTime2) { this.golsTime2 = golsTime2; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Estadio getEstadio() { return estadio; }
    public void setEstadio(Estadio estadio) { this.estadio = estadio; }

    public CidadeSede getCidade() { return cidade; }
    public void setCidade(CidadeSede cidade) { this.cidade = cidade; }
}
