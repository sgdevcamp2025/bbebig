import dayjs from 'dayjs'
import timezone from 'dayjs/plugin/timezone'
import utc from 'dayjs/plugin/utc'

dayjs.extend(utc)
dayjs.extend(timezone)

function timeHelper(timestamp: string) {
  const date = dayjs(timestamp).tz('Asia/Seoul')
  const today = dayjs().tz('Asia/Seoul')
  const isToday = date.isSame(today, 'day')

  const hours = date.hour()
  const minutes = date.minute().toString().padStart(2, '0')
  const ampm = hours >= 12 ? '오후' : '오전'
  const hour12 = hours % 12 || 12
  const year = date.year()
  const month = date.month() + 1
  const day = date.date()

  return {
    date: date.format(),
    today: today.format(),
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
