import { RefObject, useEffect } from 'react'

/**
 * 외부 클릭 이벤트 핸들러
 * @param ref 외부 클릭 이벤트 핸들러를 적용할 요소
 * @param handler 외부 클릭 이벤트 핸들러
 */
function useClickOutside(ref: RefObject<HTMLElement>, handler: () => void) {
  useEffect(() => {
    const listener = (event: MouseEvent | TouchEvent) => {
      if (!ref.current || ref.current.contains(event.target as Node)) {
        return
      }
      handler()
    }

    document.addEventListener('mousedown', listener)
    document.addEventListener('touchstart', listener)

    return () => {
      document.removeEventListener('mousedown', listener)
      document.removeEventListener('touchstart', listener)
    }
  }, [ref, handler])
}

export default useClickOutside
