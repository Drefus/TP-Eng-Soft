package com.evento.domain.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrador extends Usuario {

    public Administrador() {}

    public Administrador(String nome, String login, String senha) {
        super(nome, login, senha);
    }
}
