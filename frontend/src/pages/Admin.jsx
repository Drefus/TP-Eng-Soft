import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { formatDate, phaseClass, statusLabel, statusClass } from '../utils/format'
import Flag from '../components/Flag'
import Spinner from '../components/Spinner'

export default function Admin() {
  const [partidas, setPartidas] = useState([])
  const [loading, setLoading] = useState(true)
  const [user, setUser] = useState(null)
  const [successMsg, setSuccessMsg] = useState(null)
  const [errorMsg, setErrorMsg] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    // Verificar autenticação
    fetch('/api/auth/me')
      .then(r => r.json())
      .then(data => {
        if (!data.authenticated || !data.admin) {
          navigate('/login')
        } else {
          setUser(data.user)
          loadPartidas()
        }
      })
      .catch(() => navigate('/login'))
  }, [navigate])

  function loadPartidas() {
    setLoading(true)
    fetch('/api/admin/painel')
      .then(r => {
        if (r.status === 401 || r.status === 403) {
          navigate('/login')
          return null
        }
        return r.json()
      })
      .then(data => {
        if (data) setPartidas(data.partidas || [])
      })
      .finally(() => setLoading(false))
  }

  async function handleSalvar(partidaId, golsTime1, golsTime2) {
    setSuccessMsg(null)
    setErrorMsg(null)

    const res = await fetch('/api/admin/resultado', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ partidaId, golsTime1, golsTime2 }),
    })

    const data = await res.json()
    if (res.ok) {
      setSuccessMsg(data.status)
      loadPartidas()
    } else {
      setErrorMsg(data.error || 'Erro ao salvar')
    }

    setTimeout(() => { setSuccessMsg(null); setErrorMsg(null) }, 4000)
  }

  async function handleLogout() {
    await fetch('/api/logout', { method: 'POST' })
    navigate('/login')
  }

  if (loading) return <Spinner />

  return (
    <>
      {/* Header */}
      <div className="admin-header">
        <div>
          <h1 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            ⚙️ Painel Administrativo
          </h1>
          <p style={{ color: 'var(--text-muted)', marginTop: '0.25rem' }}>
            Gerencie resultados e classificações do evento.
          </p>
        </div>
        <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
          <span style={{
            padding: '0.25rem 0.75rem',
            background: 'rgba(16,185,129,0.15)',
            color: 'var(--success)',
            borderRadius: '9999px',
            fontSize: '0.8rem',
            fontWeight: 600
          }}>
            🟢 {user}
          </span>
          <button onClick={handleLogout} className="btn btn-outline btn-sm">🚪 Sair</button>
        </div>
      </div>

      {/* Alertas */}
      {successMsg && <div className="alert alert-success">✅ {successMsg}</div>}
      {errorMsg && <div className="alert alert-error">❌ {errorMsg}</div>}

      {/* Lista de partidas */}
      <h2 className="section-title" style={{ marginBottom: '1rem' }}>
        <span className="icon">📋</span> Atualizar Resultados
      </h2>

      <div className="admin-match-list">
        {partidas.map(p => (
          <AdminMatchItem key={p.id} partida={p} onSalvar={handleSalvar} />
        ))}
      </div>
    </>
  )
}

function AdminMatchItem({ partida, onSalvar }) {
  const [g1, setG1] = useState(partida.golsTime1 ?? 0)
  const [g2, setG2] = useState(partida.golsTime2 ?? 0)

  return (
    <div className="admin-match-item">
      <div className="admin-match-teams">
        <span className={`match-phase ${phaseClass(partida.fase)}`}
              style={{ fontSize: '0.7rem', padding: '2px 8px' }}>
          {partida.fase}
        </span>
        <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>
          {formatDate(partida.data)}
        </span>
        <Flag codigoPais={partida.time1?.codigoPais} bandeira={partida.time1?.bandeira} size="1.3rem" />
        <span style={{ fontWeight: 600 }}>{partida.time1?.nome}</span>
        <span style={{ color: 'var(--text-muted)' }}>vs</span>
        <span style={{ fontWeight: 600 }}>{partida.time2?.nome}</span>
        <Flag codigoPais={partida.time2?.codigoPais} bandeira={partida.time2?.bandeira} size="1.3rem" />
      </div>

      <div className="admin-form">
        <input
          type="number"
          min="0"
          max="99"
          value={g1}
          onChange={e => setG1(Math.max(0, parseInt(e.target.value) || 0))}
        />
        <span className="vs">×</span>
        <input
          type="number"
          min="0"
          max="99"
          value={g2}
          onChange={e => setG2(Math.max(0, parseInt(e.target.value) || 0))}
        />
        <button
          className="btn btn-primary btn-sm"
          onClick={() => onSalvar(partida.id, g1, g2)}
        >
          💾 Salvar
        </button>
      </div>

      <span className={`status-badge ${statusClass(partida.status)}`}>
        {statusLabel(partida.status)}
      </span>
    </div>
  )
}
