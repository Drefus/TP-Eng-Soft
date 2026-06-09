import { NavLink } from 'react-router-dom'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const [open, setOpen] = useState(false)
  const { user } = useAuth()

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
            {user?.admin ? (
              <NavLink to="/admin" className="nav-admin" onClick={() => setOpen(false)}>
                ⚙️ Admin
              </NavLink>
            ) : (
              <NavLink to="/login" className="nav-admin" onClick={() => setOpen(false)}>
                🔐 Login
              </NavLink>
            )}
          </li>
        </ul>
      </div>
    </nav>
  )
}
