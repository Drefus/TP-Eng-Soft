// Base URL — em dev usa o proxy do Vite; em prod o Spring Boot serve tudo na porta 8080
const BASE = ''

async function get(path) {
  const res = await fetch(BASE + path)
  if (!res.ok) throw new Error(`HTTP ${res.status} em ${path}`)
  return res.json()
}

export const api = {
  stats:         ()       => get('/api/stats'),
  partidas:      (params) => get('/api/partidas' + toQuery(params)),
  partidaDetalhe:(id)     => get(`/api/partidas/${id}`),
  selecoes:      ()       => get('/api/selecoes'),
  selecaoDetalhe:(id)     => get(`/api/selecoes/${id}`),
  cidades:       ()       => get('/api/cidades'),
  cidadeDetalhe: (id)     => get(`/api/cidades/${id}`),
  chaveamento:   ()       => get('/api/chaveamento'),
}

function toQuery(params) {
  if (!params) return ''
  const q = new URLSearchParams()
  Object.entries(params).forEach(([k, v]) => { if (v != null && v !== '') q.set(k, v) })
  const s = q.toString()
  return s ? '?' + s : ''
}
