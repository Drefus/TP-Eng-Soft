import { Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch'
import { api } from '../services/api'
import { formatCapacidade } from '../utils/format'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'

export default function Cidades() {
  const { data: cidades, loading, error } = useFetch(() => api.cidades())

  if (loading) return <Spinner />
  if (error)   return <ErrorMessage message={error} />

  return (
    <>
      <div className="page-header">
        <h1>🏙️ Cidades-Sede</h1>
        <p className="description">
          Conheça as cidades que sediam o evento, seus estádios, aeroportos e rede hoteleira.
        </p>
      </div>

      <div className="card-grid">
        {(cidades || []).map(c => (
          <Link key={c.id} to={`/cidades/${c.id}`} className="city-card">
            <div className="city-card-image">
              <span>🏙️</span>
            </div>
            <div className="city-card-body">
              <h3>{c.nome}</h3>
              <p className="country">{c.pais}</p>
              {c.estadio && (
                <>
                  <div className="city-info-row">
                    <span>🏟️</span>
                    <span>{c.estadio.nome}</span>
                  </div>
                  <div className="city-info-row">
                    <span>👥</span>
                    <span>Capacidade: {formatCapacidade(c.estadio.capacidade)}</span>
                  </div>
                </>
              )}
              <div className="city-info-row">
                <span>🏨</span>
                <span>{c.hoteis?.length ?? 0} hotéis cadastrados</span>
              </div>
              <div className="city-info-row">
                <span>✈️</span>
                <span>{c.aeroportos?.length ?? 0} aeroportos</span>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </>
  )
}
