import { NavLink } from 'react-router-dom'
import { useState } from 'react'

export default function Navbar() {
  const [open, setOpen] = useState(false)

  return (
    <nav className="navbar">
      <div className="container">
        <NavLink to="/" className="navbar-brand" onClick={() => setOpen(false)}>
          <span className="icon">⚽</span>
        </NavLink>

        <button className="navbar-toggle" onClick={() => setOpen(o => !o)}>☰</button>

        <ul className={`navbar-nav${open ? ' active' : ''}`}>
          {[
            ['/', '🏠 Início'],
            ['/partidas', '📅 Partidas'],
            ['/selecoes', '🏆 Seleções'],
            ['/cidades', '🏙️ Cidades'],
            ['/chaveamento', '📊 Chaveamento'],
          ].map(([to, label]) => (
            <li key={to}>
              <NavLink
                to={to}
                end={to === '/'}
                className={({ isActive }) => isActive ? 'active' : ''}
                onClick={() => setOpen(false)}
              >
                {label}
              </NavLink>
            </li>
          ))}
          <li>
            <NavLink to="/login" className="nav-admin">⚙️ Admin</NavLink>
          </li>
        </ul>
      </div>
    </nav>
  )
}
