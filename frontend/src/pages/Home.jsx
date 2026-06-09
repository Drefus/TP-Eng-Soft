import { useEffect, useRef } from 'react'
import { Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import MatchCard from '../components/MatchCard'
import GroupTable from '../components/GroupTable'

function StatCard({ icon, value, label }) {
  const ref = useRef(null)

  useEffect(() => {
    if (!ref.current || value == null) return
    let current = 0
    const target = Number(value)
    const step = target / 30
    const timer = setInterval(() => {
      current = Math.min(current + step, target)
      if (ref.current) ref.current.textContent = Math.floor(current)
      if (current >= target) clearInterval(timer)
    }, 30)
    return () => clearInterval(timer)
  }, [value])

  return (
    <div className="stat-card">
      <div className="stat-icon">{icon}</div>
      <div className="stat-number" ref={ref}>0</div>
      <div className="stat-label">{label}</div>
    </div>
  )
}

export default function Home() {
  const { data, loading, error } = useFetch(() => api.stats())

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />

  const proximas = (data.proximasPartidas || []).slice(0, 4)

  return (
    <>
      {/* Hero */}
      <section className="hero">
        <span className="hero-icon">🏆</span>
        <h1>Guia da Copa 2026</h1>
        <p className="subtitle">
          Acompanhe todas as partidas, seleções, cidades-sede e o chaveamento
          do maior evento esportivo do mundo.
        </p>
        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
          <Link to="/partidas" className="btn btn-primary">📅 Ver Partidas</Link>
          <Link to="/chaveamento" className="btn btn-outline">📊 Chaveamento</Link>
        </div>
      </section>

      {/* Stats */}
      <section className="stats-row">
        <StatCard icon="⚽" value={data.totalPartidas}       label="Partidas Totais" />
        <StatCard icon="✅" value={data.partidasFinalizadas} label="Finalizadas" />
        <StatCard icon="🏴" value={data.totalSelecoes}       label="Seleções" />
        <StatCard icon="🏙️" value={data.totalCidades}        label="Cidades-Sede" />
      </section>

      {/* Próximas partidas */}
      <section className="mt-2">
        <div className="section-header">
          <h2 className="section-title"><span className="icon">📅</span> Próximas Partidas</h2>
          <Link to="/partidas" className="btn btn-outline btn-sm">Ver todas →</Link>
        </div>

        {proximas.length > 0 ? (
          <div className="card-grid">
            {proximas.map(p => <MatchCard key={p.id} partida={p} showStatus={false} />)}
          </div>
        ) : (
          <div className="empty-state">
            <div className="icon">📅</div>
            <p>Nenhuma partida agendada no momento.</p>
          </div>
        )}
      </section>

      {/* Grupos */}
      <section className="mt-3">
        <div className="section-header">
          <h2 className="section-title"><span className="icon">📋</span> Classificação dos Grupos</h2>
          <Link to="/selecoes" className="btn btn-outline btn-sm">Ver seleções →</Link>
        </div>
        <div className="card-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))' }}>
          {Object.entries(data.grupos || {}).map(([grupo, sels]) => (
            <GroupTable key={grupo} grupo={grupo} selecoes={sels} />
          ))}
        </div>
      </section>
    </>
  )
}
