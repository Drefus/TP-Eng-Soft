import { useState } from 'react'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import MatchCard from '../components/MatchCard'

export default function Partidas() {
  const [filtros, setFiltros] = useState({ data: '', selecaoId: '', cidadeId: '' })
  const [aplicados, setAplicados] = useState({})

  // Carrega seleções e cidades para os selects (uma vez só)
  const { data: selecoes } = useFetch(() => api.selecoes())
  const { data: cidades }  = useFetch(() => api.cidades())

  // Carrega partidas reativamente ao filtro aplicado
  const { data: partidas, loading, error } = useFetch(
    () => api.partidas(aplicados),
    [JSON.stringify(aplicados)]
  )

  function handleSubmit(e) {
    e.preventDefault()
    setAplicados({ ...filtros })
  }

  function handleLimpar() {
    setFiltros({ data: '', selecaoId: '', cidadeId: '' })
    setAplicados({})
  }

  const temFiltro = aplicados.data || aplicados.selecaoId || aplicados.cidadeId

  return (
    <>
      <div className="page-header">
        <h1>📅 Tabela de Jogos</h1>
        <p className="description">
          Confira todas as partidas do evento. Use os filtros para encontrar jogos específicos.
        </p>
      </div>

      {/* Filtros */}
      <form className="filter-bar" onSubmit={handleSubmit}>
        <div className="filter-group">
          <label>📆 Data</label>
          <input
            type="date"
            value={filtros.data}
            onChange={e => setFiltros(f => ({ ...f, data: e.target.value }))}
          />
        </div>

        <div className="filter-group">
          <label>🏴 Seleção</label>
          <select
            value={filtros.selecaoId}
            onChange={e => setFiltros(f => ({ ...f, selecaoId: e.target.value }))}
          >
            <option value="">Todas as seleções</option>
            {(selecoes?.todas || []).map(s => (
              <option key={s.id} value={s.id}>
                {s.bandeira ? s.bandeira + ' ' : ''}{s.nome}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label>🏙️ Cidade</label>
          <select
            value={filtros.cidadeId}
            onChange={e => setFiltros(f => ({ ...f, cidadeId: e.target.value }))}
          >
            <option value="">Todas as cidades</option>
            {(cidades || []).map(c => (
              <option key={c.id} value={c.id}>{c.nome}</option>
            ))}
          </select>
        </div>

        <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'flex-end' }}>
          <button type="submit" className="btn btn-primary">🔍 Filtrar</button>
          {temFiltro && (
            <button type="button" className="btn btn-outline" onClick={handleLimpar}>
              ✕ Limpar
            </button>
          )}
        </div>
      </form>

      {/* Resultados */}
      {loading && <Spinner />}
      {error   && <ErrorMessage message={error} />}

      {!loading && !error && (
        partidas && partidas.length > 0 ? (
          <div className="card-grid">
            {partidas.map(p => <MatchCard key={p.id} partida={p} />)}
          </div>
        ) : (
          <div className="empty-state">
            <div className="icon">🔍</div>
            <p>Nenhuma partida encontrada com os filtros selecionados.</p>
            {temFiltro && (
              <button className="btn btn-outline" style={{ marginTop: '1rem' }} onClick={handleLimpar}>
                Limpar filtros
              </button>
            )}
          </div>
        )
      )}
    </>
  )
}
