import { createContext, useContext, useState, useEffect } from 'react'
import { api } from '../services/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    // Recupera do sessionStorage ao inicializar
    const saved = sessionStorage.getItem('auth_user')
    return saved ? JSON.parse(saved) : null
  })
  const [checking, setChecking] = useState(true)

  useEffect(() => {
    // Verificar se a sessão ainda é válida no backend
    api.me()
      .then(data => {
        if (data.authenticated) {
          const u = { name: data.user, admin: data.admin }
          setUser(u)
          sessionStorage.setItem('auth_user', JSON.stringify(u))
        } else {
          setUser(null)
          sessionStorage.removeItem('auth_user')
        }
      })
      .catch(() => {
        setUser(null)
        sessionStorage.removeItem('auth_user')
      })
      .finally(() => setChecking(false))
  }, [])

  async function login(username, password) {
    const res = await api.login(username, password)
    if (res.ok) {
      const data = await res.json()
      const u = { name: data.user, admin: true }
      setUser(u)
      sessionStorage.setItem('auth_user', JSON.stringify(u))
      return { success: true }
    } else {
      const data = await res.json().catch(() => ({}))
      return { success: false, error: data.error || 'Login ou senha inválidos' }
    }
  }

  async function logout() {
    await api.logout()
    setUser(null)
    sessionStorage.removeItem('auth_user')
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, checking }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
