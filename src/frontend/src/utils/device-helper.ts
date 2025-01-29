function deviceHelper() {
  const isBrowser = typeof window !== 'undefined' && typeof navigator !== 'undefined'

  if (!isBrowser) {
    return {
      isBrowser: false,
      isMac: false,
      isWindows: false
    }
  }

  const userAgent = navigator.userAgent.toLowerCase()
  const isMac = userAgent.includes('mac')
  const isWindows = userAgent.includes('win')

  return {
    isBrowser,
    isMac,
    isWindows
  }
}

export default deviceHelper()
