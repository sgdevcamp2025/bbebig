function deviceHelper() {
  const isMac = navigator.platform.toLowerCase().includes('mac')
  const isWindows = navigator.platform.toLowerCase().includes('win')

  return {
    isMac,
    isWindows
  }
}

export default deviceHelper()
