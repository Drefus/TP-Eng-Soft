const BASE = ''

async function get(path) {
  const res = await fetch(BASE + path, { credentials: 'include' })
  if (!res.ok) throw new Error(`HTTP ${res.status} em ${path}`)
  return res.json()
}

async function post(path, body) {
  const res = await fetch(BASE + path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })
  return res
}

export const api = {
  // Público
  stats:          ()       => get('/api/stats'),
  partidas:       (params) => get('/api/partidas' + toQuery(params)),
  partidaDetalhe: (id)     => get(`/api/partidas/${id}`),
  selecoes:       ()       => get('/api/selecoes'),
  selecaoDetalhe: (id)     => get(`/api/selecoes/${id}`),
  cidades:        ()       => get('/api/cidades'),
  cidadeDetalhe:  (id)     => get(`/api/cidades/${id}`),
  chaveamento:    ()       => get('/api/chaveamento'),

  // Auth
  login: (username, password) => {
    const body = new URLSearchParams({ username, password })
    return fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      credentials: 'include',
      body: body.toString(),
    })
  },
  logout: () => fetch('/api/logout', { method: 'POST', credentials: 'include' }),
  me:     () => get('/api/auth/me'),

  // Admin
  adminPainel:    ()   => get('/api/admin/painel'),
  adminResultado: (b)  => post('/api/admin/resultado', b),
  adminCidade:    (b)  => post('/api/admin/cidade/editar', b),
  adminSync:      ()   => post('/api/admin/sync', {}),
}

function toQuery(params) {
  if (!params) return ''
  const q = new URLSearchParams()
  Object.entries(params).forEach(([k, v]) => { if (v != null && v !== '') q.set(k, v) })
  const s = q.toString()
  return s ? '?' + s : ''
}
