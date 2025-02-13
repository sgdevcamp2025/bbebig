import { ComponentPropsWithoutRef, ReactNode } from 'react'

import HeaderToolBar from '../header-tool-bar'

interface Props {
  children: ReactNode
  type: 'DEFAULT' | 'VOICE' | 'DM' | 'FRIEND'
  isStatusBarOpen?: boolean
  onToggleStatusBar?: () => void
  onClose?: () => void
  searchProps?: {
    value?: string
    onChange?: ComponentPropsWithoutRef<'input'>['onChange']
    handleClear: () => void
    placeholder?: string
  }
  className?: string
}

function CommonHeader({
  children,
  type,
  isStatusBarOpen,
  onToggleStatusBar,
  onClose,
  searchProps
}: Props) {
  return (
    <div className='h-12 flex items-center px-4  border-b border-discord-gray-800 justify-between'>
      <div className='flex items-center gap-2'>{children}</div>
      <HeaderToolBar
        type={type}
        isStatusBarOpen={isStatusBarOpen}
        onToggleStatusBar={onToggleStatusBar}
        onClose={onClose}
        searchProps={searchProps}
      />
    </div>
  )
}

export default CommonHeader
