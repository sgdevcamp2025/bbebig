import { CustomPresenceStatus } from '@/types/user'

const statusKo = {
  ONLINE: '온라인',
  OFFLINE: '오프라인',
  INVISIBLE: '오프라인 표시',
  NOT_DISTURB: '방해 금지'
} as Record<CustomPresenceStatus, string>

export { statusKo }
