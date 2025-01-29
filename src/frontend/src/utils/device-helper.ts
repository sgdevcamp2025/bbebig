function deviceHelper() {
  const isBrowser = typeof window !== 'undefined' && typeof navigator !== 'undefined'

  if (!isBrowser) {
    return {
      isMac: false,
      isWindows: false
    }
  }

  const platform = navigator.platform?.toLowerCase() ?? ''
  const isMac = platform.includes('mac')
  const isWindows = platform.includes('win')

  return {
    isBrowser,
    isMac,
    isWindows
  }
}

export default deviceHelper()
