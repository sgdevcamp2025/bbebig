const COOKIE_EXPIRE_TIME = 1

function cookie() {
  const getCookie = (name: string) => {
    const cookies = document.cookie.split('; ')
    const cookie = cookies.find((cookie) => cookie.startsWith(`${name}=`))
    return cookie ? cookie.split('=')[1] : null
  }

  const setCookie = (name: string, value: string) => {
    try {
      const expires = new Date(Date.now() + COOKIE_EXPIRE_TIME * 24 * 60 * 60 * 1000).toUTCString()
      document.cookie = `${name}=${value}; path=/; expires=${expires}; httpOnly; samesite=strict`

      const savedCookie = getCookie(name)
      if (savedCookie !== value) {
        localStorage.setItem(name, value)
        console.log('localStorage에 저장됨')
      }
    } catch (error) {
      console.error('쿠키 설정 실패:', error)
    }
  }

  const deleteCookie = (name: string) => {
    document.cookie = `${name}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT`
  }

  const clearAllCookies = () => {
    document.cookie.split(';').forEach((cookie) => {
      document.cookie = cookie
        .replace(/^ +/, '')
        .replace(/=.*/, `=;expires=${new Date().toUTCString()};`)
    })
  }

  return {
    getCookie,
    setCookie,
    deleteCookie,
    clearAllCookies
  }
}

export default cookie()
