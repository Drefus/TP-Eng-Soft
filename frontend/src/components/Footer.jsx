import { Link } from 'react-router-dom'

export default function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <p className="footer-text">© 2026 Guia da Copa — Engenharia de Software.</p>
        <div className="footer-links">
          <Link to="/partidas">Partidas</Link>
          <Link to="/selecoes">Seleções</Link>
          <Link to="/cidades">Cidades</Link>
        </div>
      </div>
    </footer>
  )
}
