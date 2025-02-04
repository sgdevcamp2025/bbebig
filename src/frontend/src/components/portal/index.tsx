import { type PropsWithChildren } from 'react'
import { createPortal } from 'react-dom'

function Portal({ children }: PropsWithChildren) {
  try {
    const dom = document.getElementById('portal') || document.body
    return createPortal(children, dom)
  } catch (error) {
    return null
  }
}

export default Portal
