import { Link } from 'react-router-dom'
import Flag from './Flag'

export default function GroupTable({ grupo, selecoes }) {
  return (
    <div className="card group-section">
      <div className="card-header">
        <h3>
          <span className="group-badge">{grupo}</span>
          <span>Grupo {grupo}</span>
        </h3>
      </div>
      <div className="table-container" style={{ border: 'none', borderRadius: 0 }}>
        <table>
          <thead>
            <tr>
              <th>Seleção</th>
              <th className="text-center">P</th>
              <th className="text-center">J</th>
              <th className="text-center">V</th>
              <th className="text-center">E</th>
              <th className="text-center">D</th>
              <th className="text-center">SG</th>
            </tr>
          </thead>
          <tbody>
            {selecoes.map(s => (
              <tr key={s.id}>
                <td>
                  <div className="team-cell">
                    <Flag codigoPais={s.codigoPais} bandeira={s.bandeira} size="1.4rem" />
                    <Link to={`/selecoes/${s.id}`}>{s.nome}</Link>
                  </div>
                </td>
                <td className="text-center" style={{ fontWeight: 700 }}>{s.pontos}</td>
                <td className="text-center">{s.jogos}</td>
                <td className="text-center" style={{ color: 'var(--success)' }}>{s.vitorias}</td>
                <td className="text-center" style={{ color: 'var(--warning)' }}>{s.empates}</td>
                <td className="text-center" style={{ color: 'var(--danger)' }}>{s.derrotas}</td>
                <td className="text-center">{s.saldoGols}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
