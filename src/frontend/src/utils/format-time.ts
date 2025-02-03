function timeHelper(timestamp: string) {
  const date = new Date(timestamp)
  const today = new Date()
  const isToday = date.toDateString() === today.toDateString()

  const hours = date.getHours()
  const minutes = date.getMinutes().toString().padStart(2, '0')
  const ampm = hours >= 12 ? '오후' : '오전'
  const hour12 = hours % 12 || 12
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  return {
    date,
    today,
    isToday,
    hours,
    minutes,
    ampm,
    hour12,
    year,
    month,
    day
  }
}

export default timeHelper
