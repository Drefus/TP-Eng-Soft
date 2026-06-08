import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import Home           from './pages/Home'
import Partidas       from './pages/Partidas'
import PartidaDetalhe from './pages/PartidaDetalhe'
import Selecoes       from './pages/Selecoes'
import SelecaoDetalhe from './pages/SelecaoDetalhe'
import Cidades        from './pages/Cidades'
import CidadeDetalhe  from './pages/CidadeDetalhe'
import Chaveamento    from './pages/Chaveamento'
import Login          from './pages/Login'
import Admin          from './pages/Admin'

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main>
        <div className="container">
          <Routes>
            <Route path="/"                 element={<Home />} />
            <Route path="/partidas"         element={<Partidas />} />
            <Route path="/partidas/:id"     element={<PartidaDetalhe />} />
            <Route path="/selecoes"         element={<Selecoes />} />
            <Route path="/selecoes/:id"     element={<SelecaoDetalhe />} />
            <Route path="/cidades"          element={<Cidades />} />
            <Route path="/cidades/:id"      element={<CidadeDetalhe />} />
            <Route path="/chaveamento"      element={<Chaveamento />} />
            <Route path="/login"            element={<Login />} />
            <Route path="/admin"            element={<Admin />} />
            <Route path="*"                 element={<Home />} />
          </Routes>
        </div>
      </main>
      <Footer />
    </BrowserRouter>
  )
}
