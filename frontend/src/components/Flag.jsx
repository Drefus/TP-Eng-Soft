/**
 * Componente de bandeira.
 * No Windows, emoji flags não renderizam corretamente — usa imagem SVG como fallback.
 * Fonte: flagcdn.com (baseado no código ISO de 2 letras)
 */
export default function Flag({ codigoPais, bandeira, size = '1.5rem' }) {
  // Se tiver código de país, usar imagem SVG (mais confiável cross-platform)
  if (codigoPais) {
    return (
      <img
        src={`https://flagcdn.com/w40/${codigoPais.toLowerCase()}.png`}
        srcSet={`https://flagcdn.com/w80/${codigoPais.toLowerCase()}.png 2x`}
        alt={codigoPais}
        style={{ width: size, height: 'auto', borderRadius: '2px', verticalAlign: 'middle' }}
      />
    )
  }

  // Fallback para emoji (funciona em macOS/Linux/móveis)
  if (bandeira) {
    return <span style={{ fontSize: size }}>{bandeira}</span>
  }

  // Sem dados
  return <span style={{ fontSize: size }}>🏳️</span>
}
