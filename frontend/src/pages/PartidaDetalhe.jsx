import { useParams, Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import { formatDate, formatTime, formatCapacidade, phaseClass, statusLabel, statusClass } from '../utils/format'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import Flag from '../components/Flag'

function MiniMatch({ p }) {
  return (
    <div style={{ padding: '0.5rem 0', borderBottom: '1px solid rgba(255,255,255,0.06)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontSize: '0.85rem' }}>
          {p.time1?.bandeira} {p.time1?.nome} <strong>{p.placar ?? '- x -'}</strong> {p.time2?.nome} {p.time2?.bandeira}
        </span>
        <span className={`status-badge ${statusClass(p.status)}`} style={{ fontSize: '0.7rem' }}>
          {p.fase}
        </span>
      </div>
    </div>
  )
}

export default function PartidaDetalhe() {
  const { id } = useParams()
  const { data, loading, error } = useFetch(() => api.partidaDetalhe(id), [id])

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />
  if (!data)   return null

  const { partida, partidasTime1, partidasTime2 } = data

  return (
    <>
      {/* Hero */}
      <section className="match-detail-hero">
        <span className={`match-phase ${phaseClass(partida.fase)}`}
              style={{ fontSize: '0.9rem', padding: '4px 16px' }}>
          {partida.fase}
        </span>

        <div className="match-detail-teams">
          <div className="match-detail-team">
            <Flag codigoPais={partida.time1?.codigoPais} bandeira={partida.time1?.bandeira} size="5rem" />
            <span className="name">{partida.time1?.nome}</span>
          </div>

          {partida.golsTime1 != null ? (
            <div className="match-detail-score">
              {partida.golsTime1} × {partida.golsTime2}
            </div>
          ) : (
            <div className="match-detail-score pending">A DEFINIR</div>
          )}

          <div className="match-detail-team">
            <Flag codigoPais={partida.time2?.codigoPais} bandeira={partida.time2?.bandeira} size="5rem" />
            <span className="name">{partida.time2?.nome}</span>
          </div>
        </div>

        <span className={`status-badge ${statusClass(partida.status)}`}
              style={{ fontSize: '0.85rem', padding: '4px 14px' }}>
          {statusLabel(partida.status)}
        </span>
      </section>

      {/* Info */}
      <div className="detail-grid">
        <div className="detail-info">
          <span style={{ fontSize: '1.5rem' }}>📅</span>
          <div>
            <div className="label">Data</div>
            <div className="value">{formatDate(partida.data)}</div>
          </div>
        </div>
        <div className="detail-info">
          <span style={{ fontSize: '1.5rem' }}>🕐</span>
          <div>
            <div className="label">Horário</div>
            <div className="value">{formatTime(partida.horario)}</div>
          </div>
        </div>
        {partida.estadio && (
          <div className="detail-info">
            <span style={{ fontSize: '1.5rem' }}>🏟️</span>
            <div>
              <div className="label">Estádio</div>
              <div className="value">{partida.estadio.nome}</div>
            </div>
          </div>
        )}
        {partida.cidade && (
          <div className="detail-info">
            <span style={{ fontSize: '1.5rem' }}>🏙️</span>
            <div>
              <div className="label">Cidade</div>
              <div className="value">{partida.cidade.nome}, {partida.cidade.pais}</div>
            </div>
          </div>
        )}
        {partida.estadio && (
          <div className="detail-info">
            <span style={{ fontSize: '1.5rem' }}>👥</span>
            <div>
              <div className="label">Capacidade</div>
              <div className="value">{formatCapacidade(partida.estadio.capacidade)} lugares</div>
            </div>
          </div>
        )}
      </div>

      {/* Histórico */}
      <section className="mt-3">
        <h2 className="section-title mb-2"><span className="icon">📊</span> Outras partidas das seleções</h2>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
          {[
            { time: partida.time1, lista: partidasTime1 },
            { time: partida.time2, lista: partidasTime2 },
          ].map(({ time, lista }) => (
            <div key={time?.id} className="card">
              <div className="card-header">
                <h3>{time?.bandeira} {time?.nome}</h3>
              </div>
              <div className="card-body">
                {lista?.length > 0
                  ? lista.map(p => <MiniMatch key={p.id} p={p} />)
                  : <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>Nenhuma outra partida.</p>
                }
              </div>
            </div>
          ))}
        </div>
      </section>

      <div className="mt-2" style={{ textAlign: 'center' }}>
        <Link to="/partidas" className="btn btn-outline">← Voltar às partidas</Link>
      </div>
    </>
  )
}
