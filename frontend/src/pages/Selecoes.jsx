import { Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import Flag from '../components/Flag'

export default function Selecoes() {
  const { data, loading, error } = useFetch(() => api.selecoes())

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />
  if (!data)   return null

  const grupos = data.grupos || {}
  const todas  = data.todas  || []

  return (
    <>
      <div className="page-header">
        <h1>🏆 Seleções Participantes</h1>
        <p className="description">Conheça todas as seleções que disputam a Copa do Mundo de 2026.</p>
      </div>

      {/* Tabelas por grupo */}
      {Object.entries(grupos).map(([grupo, sels]) => (
        <div key={grupo} className="group-section">
          <div className="group-header">
            <span className="group-badge">{grupo}</span>
            <span>Grupo {grupo}</span>
          </div>

          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Seleção</th>
                  <th className="text-center">Pts</th>
                  <th className="text-center">J</th>
                  <th className="text-center">V</th>
                  <th className="text-center">E</th>
                  <th className="text-center">D</th>
                  <th className="text-center">GP</th>
                  <th className="text-center">GC</th>
                  <th className="text-center">SG</th>
                </tr>
              </thead>
              <tbody>
                {sels.map((s, i) => (
                  <tr key={s.id}>
                    <td>
                      <span style={{ fontWeight: 700, color: i < 2 ? 'var(--success)' : 'var(--text-muted)' }}>
                        {i + 1}
                      </span>
                    </td>
                    <td>
                      <Link to={`/selecoes/${s.id}`} className="team-cell">
                        <Flag codigoPais={s.codigoPais} bandeira={s.bandeira} size="1.4rem" />
                        <span>{s.nome}</span>
                      </Link>
                    </td>
                    <td className="text-center" style={{ fontWeight: 700 }}>{s.pontos}</td>
                    <td className="text-center">{s.jogos}</td>
                    <td className="text-center" style={{ color: 'var(--success)' }}>{s.vitorias}</td>
                    <td className="text-center" style={{ color: 'var(--warning)' }}>{s.empates}</td>
                    <td className="text-center" style={{ color: 'var(--danger)' }}>{s.derrotas}</td>
                    <td className="text-center">{s.golsPro}</td>
                    <td className="text-center">{s.golsContra}</td>
                    <td className="text-center" style={{ fontWeight: 600 }}>{s.saldoGols}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ))}

      {/* Grid de cards */}
      <h2 className="section-title mt-3 mb-2"><span className="icon">🌍</span> Todas as Seleções</h2>
      <div className="card-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))' }}>
        {todas.map(s => (
          <Link key={s.id} to={`/selecoes/${s.id}`} className="team-card">
            <Flag codigoPais={s.codigoPais} bandeira={s.bandeira} size="3.5rem" />
            <h3>{s.nome}</h3>
            <span className="group-tag">Grupo {s.grupo}</span>
          </Link>
        ))}
      </div>
    </>
  )
}
