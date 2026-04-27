# Projeto Arquitetural e Modelagem UML --- Sistema de Evento Esportivo

Este documento apresenta o **projeto arquitetural** e a **modelagem
UML** do sistema de acompanhamento de eventos esportivos.\
Cada diagrama foi criado a partir dos requisitos funcionais e não
funcionais definidos na especificação do projeto.

------------------------------------------------------------------------

## 🏗️ Projeto Arquitetural --- Diagrama de Componentes

### 📌 Decisão de Arquitetura

Foi adotada uma **Arquitetura em Camadas (Layered Architecture)**
porque:

-   Facilita manutenção e evolução do sistema (RNF07).
-   Permite separação clara entre interface, regras de negócio e
    persistência.
-   Reduz acoplamento entre módulos.
-   Suporta futura integração com APIs externas.

### 📌 Responsabilidades das Camadas

-   **Web** → Interface acessada por usuários e administradores.
-   **Application Layer** → Orquestra casos de uso.
-   **Domain Layer** → Contém regras de negócio do evento esportivo.
-   **Infrastructure Layer** → Persistência e comunicação externa.
-   **External** → Serviços externos opcionais via API.

``` mermaid
flowchart LR

User[Usuário]
Admin[Administrador]

subgraph Web
    UI[Interface Web]
end

subgraph Application Layer
    GameController
    TeamController
    CityController
    BracketController
    AdminController
end

subgraph Domain Layer
    GameService
    TeamService
    CityService
    BracketService
    AuthService
end

subgraph Infrastructure Layer
    Database[(Banco de Dados)]
    Repository
end

subgraph External
    API[API Esportiva Externa]
end

User --> UI
Admin --> UI

UI --> GameController
UI --> TeamController
UI --> CityController
UI --> BracketController
UI --> AdminController

GameController --> GameService
TeamController --> TeamService
CityController --> CityService
BracketController --> BracketService
AdminController --> AuthService

GameService --> Repository
TeamService --> Repository
CityService --> Repository
BracketService --> Repository

Repository --> Database

GameService --> API
```

------------------------------------------------------------------------

## 🎯 Diagrama de Casos de Uso

### 📌 Decisão de Modelagem

O diagrama identifica **quem interage com o sistema** e **quais
funcionalidades são oferecidas**.

Foram definidos dois atores:

-   **Usuário** → Consulta informações do evento.
-   **Administrador** → Gerencia resultados e classificação.

A separação reforça o requisito de **segurança e autenticação**.

``` mermaid
flowchart LR

User((Usuário))
Admin((Administrador))

User --> ViewGames[Visualizar tabela de jogos]
User --> FilterGames[Filtrar partidas]
User --> ViewTeams[Consultar seleções]
User --> ViewCities[Consultar cidades-sede]
User --> ViewBracket[Visualizar chaveamento]
User --> ViewMatch[Ver detalhes da partida]

Admin --> Login[Autenticar]
Admin --> UpdateResult[Atualizar resultados]
Admin --> DefineQualified[Definir classificados]
```

### 📝 Descrição dos Cenários de Casos de Uso

Abaixo estão os cenários detalhados para todos os casos de uso do sistema, integrando as funcionalidades de consulta e os controles administrativos:

#### **Atores do Usuário (Consulta)**

* **UC01: Visualizar tabela de jogos (RF01)**:
    * **Ator**: Usuário.
    * **Fluxo Principal**: O sistema exibe a tabela completa com data, horário e local de cada partida.
    * **Pós-condição**: O usuário visualiza o cronograma oficial do evento.

* **UC02: Filtrar partidas (RF05)**:
    * **Ator**: Usuário.
    * **Fluxo Principal**: O usuário seleciona critérios de busca por data, seleção específica ou cidade-sede.
    * **Pós-condição**: O sistema apresenta apenas os jogos que correspondem aos filtros aplicados.

* **UC03: Consultar seleções (RF03)**:
    * **Ator**: Usuário.
    * **Fluxo Principal**: O sistema gera uma lista de todas as seleções participantes e suas informações básicas.
    * **Pós-condição**: O usuário acessa os dados das equipes que disputam o torneio.

* **UC04: Consultar cidades-sede (RF02)**:
    * **Ator**: Usuário.
    * **Fluxo Principal**: O sistema exibe um guia da sede selecionada, incluindo estádio, aeroportos próximos e rede hoteleira.
    * **Pós-condição**: O usuário obtém as informações logísticas necessárias para o deslocamento nas sedes.

* **UC05: Visualizar chaveamento (RF04)**:
    * **Ator**: Usuário[cite: 15, 150].
    * **Fluxo Principal**: O sistema apresenta graficamente a estrutura das fases eliminatórias e os cruzamentos futuros.
    * **Pós-condição**: O usuário compreende o caminho das equipes até a final.

* **UC06: Ver detalhes da partida (RF08)**:
    * **Ator**: Usuário.
    * **Fluxo Principal**: Ao clicar em um jogo específico, o sistema detalha estatísticas e informações extras da partida.
    * **Pós-condição**: O usuário acessa a ficha técnica completa do confronto.

#### **Atores do Administrador (Gestão)**

* **UC07: Autenticar (RNF03)**:
    * **Ator**: Administrador.
    * **Fluxo Principal**: O administrador insere login e senha na área restrita.
    * **Pós-condição**: O acesso às funções de edição é liberado mediante validação de segurança.

* **UC08: Atualizar resultados (RF06)**:
    * **Ator**: Administrador.
    * **Pré-condição**: O administrador deve estar devidamente autenticado.
    * **Fluxo Principal**: O administrador insere os placares das partidas encerradas no sistema.
    * **Pós-condição**: Os dados são salvos e atualizados em tempo real na tabela pública.

* **UC09: Definir classificados (RF06)**:
    * **Ator**: Administrador.
    * **Fluxo Principal**: O administrador confirma quais seleções avançam para as próximas fases com base nos resultados.
    * **Pós-condição**: O gráfico de chaveamento é reordenado com as seleções vitoriosas.

------------------------------------------------------------------------

## 🧠 Diagrama de Classes

### 📌 Decisão de Modelagem

O modelo de domínio foi construído a partir das **entidades centrais do
evento esportivo**:

-   Seleções participantes
-   Partidas
-   Cidades-sede
-   Estrutura logística
-   Chaveamento eliminatório

Principais decisões:

-   **Administrador herda de Usuário** → reutilização de atributos.
-   **Partida relaciona duas seleções**.
-   **CidadeSede agrega infraestrutura** (estádio, hotel, aeroporto).
-   **Chaveamento organiza fases eliminatórias**.

``` mermaid
classDiagram

class Usuario {
    +id
    +nome
}

class Administrador {
    +login()
    +atualizarResultado()
}

Usuario <|-- Administrador

class Selecao {
    +id
    +nome
    +grupo
    +tecnico
}

class CidadeSede {
    +nome
    +pais
}

class Estadio {
    +nome
    +capacidade
}

CidadeSede "1" --> "1" Estadio

class Partida {
    +id
    +data
    +horario
    +resultado
}

Selecao "2" --> "0..*" Partida

class Chaveamento {
    +fase
    +gerarChaveamento()
}

Partida --> Chaveamento

class Hotel {
    +nome
}

class Aeroporto {
    +nome
}

CidadeSede --> Hotel
CidadeSede --> Aeroporto
```

------------------------------------------------------------------------

## 🔄 Diagrama de Sequência --- Atualizar Resultado

### 📌 Decisão de Modelagem

Este diagrama representa o fluxo crítico administrativo:

1.  Administrador envia resultado.
2.  Sistema valida autenticação.
3.  Regra de negócio atualiza dados.
4.  Persistência salva alteração.

Motivos:

-   Evidenciar requisito de **segurança**.
-   Mostrar separação Controller → Service → Repository.
-   Demonstrar fluxo real de backend.

``` mermaid
sequenceDiagram

Administrador->>UI: inserir resultado
UI->>AdminController: atualizarResultado()
AdminController->>AuthService: validarLogin()
AuthService-->>AdminController: OK

AdminController->>GameService: atualizarResultado()
GameService->>Repository: salvarResultado()
Repository->>Database: update

Database-->>Repository: OK
Repository-->>GameService: OK
GameService-->>UI: resultado atualizado
```

------------------------------------------------------------------------

## 📦 Diagrama de Pacotes

### 📌 Decisão de Modelagem

O diagrama organiza o código em módulos independentes:

-   **presentation** → interface do usuário.
-   **application** → controladores.
-   **domain** → entidades e regras.
-   **infrastructure** → banco e integrações.

Benefícios:

-   Alta coesão.
-   Baixo acoplamento.
-   Facilita testes e manutenção.

``` mermaid
flowchart TB

subgraph presentation
    ui
end

subgraph application
    controllers
end

subgraph domain
    entities
    services
end

subgraph infrastructure
    repository
    database
    api
end

ui --> controllers
controllers --> services
services --> entities
services --> repository
repository --> database
services --> api
```

------------------------------------------------------------------------

## ✅ Conclusão Arquitetural

A combinação dos diagramas garante:

-   Alinhamento entre requisitos e implementação.
-   Separação clara de responsabilidades.
-   Base sólida para evolução futura do sistema.
