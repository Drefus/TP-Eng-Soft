import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api':    { target: 'http://localhost:8080', changeOrigin: true },
      '/login':  { target: 'http://localhost:8080', changeOrigin: true },
      '/logout': { target: 'http://localhost:8080', changeOrigin: true },
      '/admin':  { target: 'http://localhost:8080', changeOrigin: true },
      // Proxy do CSS legado (usado apenas em dev se necessário)
      '/css':    { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
  build: {
    // Gera o build na pasta static do Spring Boot
    // emptyOutDir: false para não apagar css/style.css e outros recursos
    outDir: '../src/main/resources/static',
    emptyOutDir: false,
  },
})
