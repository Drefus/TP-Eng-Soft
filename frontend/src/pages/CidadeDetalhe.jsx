import { useParams, Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import { formatCapacidade } from '../utils/format'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import MatchCard from '../components/MatchCard'

function Stars({ n }) {
  return (
    <div>
      {Array.from({ length: 5 }, (_, i) => (
        <span key={i} style={{ color: i < n ? '#fbbf24' : 'var(--text-muted)' }}>★</span>
      ))}
    </div>
  )
}

export default function CidadeDetalhe() {
  const { id } = useParams()
  const { data, loading, error } = useFetch(() => api.cidadeDetalhe(id), [id])

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />
  if (!data)   return null

  const { cidade, partidas } = data

  return (
    <>
      <div className="detail-header">
        <span className="flag-large">🏙️</span>
        <h1>{cidade.nome}</h1>
        <p className="meta">{cidade.pais}</p>
      </div>

      {cidade.descricao && (
        <p style={{
          textAlign: 'center', color: 'var(--text-secondary)',
          maxWidth: '700px', margin: '0 auto 3rem'
        }}>
          {cidade.descricao}
        </p>
      )}

      {/* Estádio */}
      {cidade.estadio && (
        <section className="mt-2">
          <h2 className="section-title mb-2"><span className="icon">🏟️</span> Estádio</h2>
          <div className="card">
            <div className="card-body">
              <div className="detail-grid" style={{ gridTemplateColumns: '1fr 1fr' }}>
                <div className="detail-info">
                  <span style={{ fontSize: '1.5rem' }}>🏟️</span>
                  <div>
                    <div className="label">Nome</div>
                    <div className="value">{cidade.estadio.nome}</div>
                  </div>
                </div>
                <div className="detail-info">
                  <span style={{ fontSize: '1.5rem' }}>👥</span>
                  <div>
                    <div className="label">Capacidade</div>
                    <div className="value">{formatCapacidade(cidade.estadio.capacidade)} lugares</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      )}

      {/* Hotéis */}
      {cidade.hoteis?.length > 0 && (
        <section className="mt-2">
          <h2 className="section-title mb-2"><span className="icon">🏨</span> Rede Hoteleira</h2>
          <div className="card-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
            {cidade.hoteis.map(h => (
              <div key={h.id} className="card">
                <div className="card-body">
                  <h4 style={{ marginBottom: '0.5rem' }}>{h.nome}</h4>
                  <Stars n={h.estrelas} />
                  {h.endereco && (
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '0.5rem' }}>
                      📍 {h.endereco}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Aeroportos */}
      {cidade.aeroportos?.length > 0 && (
        <section className="mt-2">
          <h2 className="section-title mb-2"><span className="icon">✈️</span> Aeroportos</h2>
          <div className="card-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
            {cidade.aeroportos.map(a => (
              <div key={a.id} className="card">
                <div className="card-body">
                  <h4 style={{ marginBottom: '0.5rem' }}>{a.nome}</h4>
                  <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                    🏷️ Código IATA: <strong style={{ color: 'var(--text-accent)' }}>{a.codigo}</strong>
                  </p>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Partidas */}
      {partidas?.length > 0 && (
        <section className="mt-3">
          <h2 className="section-title mb-2"><span className="icon">⚽</span> Partidas nesta cidade</h2>
          <div className="card-grid">
            {partidas.map(p => <MatchCard key={p.id} partida={p} />)}
          </div>
        </section>
      )}

      <div className="mt-3" style={{ textAlign: 'center' }}>
        <Link to="/cidades" className="btn btn-outline">← Voltar às cidades</Link>
      </div>
    </>
  )
}
