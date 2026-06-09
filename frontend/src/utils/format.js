export function formatDate(dateStr) {
  if (!dateStr) return ''
  const [year, month, day] = dateStr.split('-')
  return `${day}/${month}/${year}`
}

export function formatTime(timeStr) {
  if (!timeStr) return ''
  return timeStr.substring(0, 5)   // "HH:mm:ss" → "HH:mm"
}

export function formatCapacidade(n) {
  if (!n) return '—'
  return n.toLocaleString('pt-BR')
}

export function phaseClass(fase) {
  if (!fase) return ''
  return 'phase-' + fase.toLowerCase().replace(/\s/g, '')
}

export function statusLabel(status) {
  switch (status) {
    case 'FINALIZADA':   return '✅ Finalizada'
    case 'AGENDADA':     return '🕐 Agendada'
    case 'EM_ANDAMENTO': return '⚡ Em andamento'
    default:             return status
  }
}

export function statusClass(status) {
  return 'status-' + (status || '').toLowerCase()
}
