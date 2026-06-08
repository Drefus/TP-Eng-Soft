# Frontend — Guia da Copa 2026 (React + Vite)

## Desenvolvimento (com hot-reload)

Requer o backend Spring Boot rodando na porta 8080.

```bash
# Terminal 1 — backend
./mvnw spring-boot:run

# Terminal 2 — frontend (porta 5173)
cd frontend
npm run dev
```

Acesse: http://localhost:5173

O Vite faz proxy de `/api/**`, `/login`, `/logout` e `/admin` para o Spring Boot automaticamente.

## Build para produção

```bash
cd frontend
npm run build
```

Gera os arquivos em `src/main/resources/static/`. Depois basta rodar o Spring Boot normalmente — ele serve o React junto.

## Estrutura

```
src/
  components/     # Navbar, Footer, MatchCard, GroupTable, Spinner, ErrorMessage
  hooks/          # useFetch — hook genérico de chamada à API
  pages/          # Home, Partidas, PartidaDetalhe, Selecoes, SelecaoDetalhe,
                  # Cidades, CidadeDetalhe, Chaveamento
  services/       # api.js — todas as chamadas fetch para /api/*
  utils/          # format.js — datas, horários, status, classes CSS
```
