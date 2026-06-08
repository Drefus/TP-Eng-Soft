export default function ErrorMessage({ message }) {
  return (
    <div className="alert alert-error">
      ❌ Erro ao carregar dados: {message}
    </div>
  )
}
