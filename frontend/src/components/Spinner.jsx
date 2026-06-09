export default function Spinner() {
  return (
    <div className="empty-state">
      <div className="hero-icon" style={{ animation: 'float 1s ease-in-out infinite' }}>⚽</div>
      <p>Carregando...</p>
    </div>
  )
}
