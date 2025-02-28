export const log = (message: string, ...args: unknown[]) =>
  console.log(`%c${message}`, 'color: #4CAF50; font-weight: bold;', ...args)
export const errorLog = (message: string, ...args: unknown[]) =>
  console.error(`%c${message}`, 'color: #FF5252; font-weight: bold;', ...args)
