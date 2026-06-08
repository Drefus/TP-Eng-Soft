import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import Flag from '../components/Flag'

function BracketMatch({ chave }) {
  if (!chave.partida) {
    return (
      <div className="bracket-match">
        <div className="bracket-team" style={{ padding: '1.5rem', textAlign: 'center' }}>
          <span style={{ color: 'var(--text-muted)' }}>A definir</span>
        </div>
      </div>
    )
  }

  const { partida } = chave
  const finalizada = partida.status === 'FINALIZADA'
  const g1 = partida.golsTime1
  const g2 = partida.golsTime2

  const winner1 = finalizada && g1 != null && g2 != null && g1 > g2
  const winner2 = finalizada && g1 != null && g2 != null && g2 > g1

  return (
    <div className="bracket-match">
      <div className={`bracket-team${winner1 ? ' winner' : ''}`}>
        <div className="bracket-team-info">
          <Flag codigoPais={partida.time1?.codigoPais} bandeira={partida.time1?.bandeira} size="1.3rem" />
          <span>{partida.time1?.nome}</span>
        </div>
        <span className="bracket-team-score">{g1 != null ? g1 : '—'}</span>
      </div>
      <div className={`bracket-team${winner2 ? ' winner' : ''}`}>
        <div className="bracket-team-info">
          <Flag codigoPais={partida.time2?.codigoPais} bandeira={partida.time2?.bandeira} size="1.3rem" />
          <span>{partida.time2?.nome}</span>
        </div>
        <span className="bracket-team-score">{g2 != null ? g2 : '—'}</span>
      </div>
    </div>
  )
}

export default function Chaveamento() {
  const { data: fases, loading, error } = useFetch(() => api.chaveamento())

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />

  const entries = Object.entries(fases || {})

  return (
    <>
      <div className="page-header" style={{ textAlign: 'center' }}>
        <h1>📊 Chaveamento Eliminatório</h1>
        <p className="description">Acompanhe o caminho das seleções até a grande final.</p>
      </div>

      {entries.length === 0 ? (
        <div className="empty-state">
          <div className="icon">📊</div>
          <p>O chaveamento ainda não foi definido.</p>
        </div>
      ) : (
        <>
          <div className="bracket-container">
            <div className="bracket">
              {entries.map(([fase, chaves]) => (
                <div key={fase} className="bracket-round">
                  <h3 className="bracket-round-title">{fase}</h3>
                  {chaves.map(chave => (
                    <BracketMatch key={chave.id} chave={chave} />
                  ))}
                </div>
              ))}
            </div>
          </div>

          {/* Legenda */}
          <div style={{
            display: 'flex', justifyContent: 'center',
            gap: '2rem', marginTop: 'var(--space-xl)', flexWrap: 'wrap'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
              <div style={{ width: 12, height: 12, borderRadius: 3, background: 'rgba(16,185,129,0.3)' }} />
              <span>Vencedor</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
              <div style={{ width: 12, height: 12, borderRadius: 3, background: 'var(--bg-card)', border: '1px solid var(--border-color)' }} />
              <span>A disputar</span>
            </div>
          </div>
        </>
      )}
    </>
  )
}
