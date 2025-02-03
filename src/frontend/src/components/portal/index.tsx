import { type PropsWithChildren } from 'react'
import { createPortal } from 'react-dom'

function Portal({ children }: PropsWithChildren) {
  const dom = document.getElementById('portal') || document.body
  return createPortal(children, dom)
}

export default Portal
