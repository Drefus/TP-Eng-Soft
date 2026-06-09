import { Link } from 'react-router-dom'
import { formatDate, formatTime, phaseClass, statusLabel, statusClass } from '../utils/format'
import Flag from './Flag'

export default function MatchCard({ partida, showStatus = true }) {
  const hasScore = partida.golsTime1 != null

  return (
    <Link to={`/partidas/${partida.id}`} className="match-card">
      <div className="match-meta">
        <span>{formatDate(partida.data)} • {formatTime(partida.horario)}</span>
        <span className={`match-phase ${phaseClass(partida.fase)}`}>{partida.fase}</span>
      </div>

      <div className="match-teams">
        <div className="match-team">
          <Flag codigoPais={partida.time1?.codigoPais} bandeira={partida.time1?.bandeira} size="2.5rem" />
          <span className="name">{partida.time1?.nome}</span>
        </div>

        {hasScore ? (
          <div className="match-score">
            <span>{partida.golsTime1}</span>
            <span className="separator">×</span>
            <span>{partida.golsTime2}</span>
          </div>
        ) : (
          <div className="match-score pending"><span>— × —</span></div>
        )}

        <div className="match-team">
          <Flag codigoPais={partida.time2?.codigoPais} bandeira={partida.time2?.bandeira} size="2.5rem" />
          <span className="name">{partida.time2?.nome}</span>
        </div>
      </div>

      {partida.estadio && (
        <div className="match-venue">
          <span>🏟️</span>
          <span>{partida.estadio.nome}</span>
          {partida.cidade && <><span>•</span><span>{partida.cidade.nome}</span></>}
        </div>
      )}

      {showStatus && (
        <div style={{ marginTop: '0.5rem' }}>
          <span className={`status-badge ${statusClass(partida.status)}`}>
            {statusLabel(partida.status)}
          </span>
        </div>
      )}
    </Link>
  )
}
