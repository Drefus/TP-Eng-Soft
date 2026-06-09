import { useParams, Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import MatchCard from '../components/MatchCard'
import Flag from '../components/Flag'

function StatCard({ icon, value, label }) {
  return (
    <div className="stat-card">
      <div className="stat-icon">{icon}</div>
      <div className="stat-number">{value}</div>
      <div className="stat-label">{label}</div>
    </div>
  )
}

export default function SelecaoDetalhe() {
  const { id } = useParams()
  const { data, loading, error } = useFetch(() => api.selecaoDetalhe(id), [id])

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />
  if (!data)   return null

  const { selecao, partidas } = data

  return (
    <>
      <div className="detail-header">
        <Flag codigoPais={selecao.codigoPais} bandeira={selecao.bandeira} size="5rem" />
        <h1 style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem', marginTop: '1rem' }}>
          <Flag codigoPais={selecao.codigoPais} bandeira={selecao.bandeira} size="2rem" />
          {selecao.nome}
        </h1>
        <p className="meta">Grupo {selecao.grupo}</p>
        {selecao.tecnico && (
          <p style={{ color: 'var(--text-secondary)', marginTop: '0.5rem', fontSize: '1rem' }}>
            🧑‍💼 Técnico: <strong>{selecao.tecnico}</strong>
          </p>
        )}
      </div>

      {/* Informações do time */}
      <div className="detail-grid" style={{ maxWidth: '600px', margin: '0 auto var(--space-xl)' }}>
        <div className="detail-info">
          <Flag codigoPais={selecao.codigoPais} bandeira={selecao.bandeira} size="1.5rem" />
          <div>
            <div className="label">Seleção</div>
            <div className="value">{selecao.nome}</div>
          </div>
        </div>
        <div className="detail-info">
          <span style={{ fontSize: '1.5rem' }}>📋</span>
          <div>
            <div className="label">Grupo</div>
            <div className="value">{selecao.grupo}</div>
          </div>
        </div>
        {selecao.codigoPais && (
          <div className="detail-info">
            <span style={{ fontSize: '1.5rem' }}>🌍</span>
            <div>
              <div className="label">Código País</div>
              <div className="value">{selecao.codigoPais}</div>
            </div>
          </div>
        )}
        <div className="detail-info">
          <span style={{ fontSize: '1.5rem' }}>🎮</span>
          <div>
            <div className="label">Jogos</div>
            <div className="value">{selecao.jogos}</div>
          </div>
        </div>
      </div>

      <div className="stats-row">
        <StatCard icon="⭐" value={selecao.pontos}   label="Pontos" />
        <StatCard icon="✅" value={selecao.vitorias} label="Vitórias" />
        <StatCard icon="🤝" value={selecao.empates}  label="Empates" />
        <StatCard icon="❌" value={selecao.derrotas} label="Derrotas" />
      </div>

      <div className="stats-row" style={{ gridTemplateColumns: 'repeat(3, 1fr)' }}>
        <StatCard icon="⚽" value={selecao.golsPro}    label="Gols Pró" />
        <StatCard icon="🥅" value={selecao.golsContra} label="Gols Contra" />
        <StatCard icon="📊" value={selecao.saldoGols}  label="Saldo de Gols" />
      </div>

      <section className="mt-3">
        <h2 className="section-title mb-2"><span className="icon">📅</span> Partidas</h2>
        {partidas?.length > 0 ? (
          <div className="card-grid">
            {partidas.map(p => <MatchCard key={p.id} partida={p} />)}
          </div>
        ) : (
          <div className="empty-state">
            <div className="icon">📅</div>
            <p>Nenhuma partida registrada.</p>
          </div>
        )}
      </section>

      <div className="mt-2" style={{ textAlign: 'center' }}>
        <Link to="/selecoes" className="btn btn-outline">← Voltar às seleções</Link>
      </div>
    </>
  )
}
