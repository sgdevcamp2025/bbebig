import { ENABLE_CONSOLE } from '@/constants/env'

export const log = (message: string, ...args: unknown[]) => {
  if (!ENABLE_CONSOLE) return
  // eslint-disable-next-line no-console
  console.log(`%c${message}`, 'color: #4CAF50; font-weight: bold;', ...args)
}
export const errorLog = (message: string, ...args: unknown[]) => {
  if (!ENABLE_CONSOLE) return
  console.error(`%c${message}`, 'color: #FF5252; font-weight: bold;', ...args)
}
