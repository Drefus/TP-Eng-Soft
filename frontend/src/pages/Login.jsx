import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const { login, user } = useAuth()
  const navigate = useNavigate()

  // Se já está logado, redireciona para admin
  if (user?.admin) {
    navigate('/admin')
    return null
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)

    const result = await login(username, password)
    if (result.success) {
      navigate('/admin')
    } else {
      setError(result.error)
    }
    setLoading(false)
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <span className="icon">🔐</span>
        <h2>Área Administrativa</h2>
        <p className="subtitle">Faça login para gerenciar o evento.</p>

        {error && <div className="alert alert-error">❌ {error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Login</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={e => setUsername(e.target.value)}
              placeholder="Digite seu login"
              required
              autoFocus
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Senha</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              placeholder="Digite sua senha"
              required
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? '⏳ Entrando...' : '🚀 Entrar'}
          </button>
        </form>

        <p style={{ marginTop: '1.5rem', fontSize: '0.8rem', color: 'var(--text-muted)' }}>
          Acesso restrito a administradores do evento.
        </p>
      </div>
    </div>
  )
}
