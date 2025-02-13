import { X } from 'lucide-react'
import { ComponentPropsWithoutRef } from 'react'

import SearchInput from '../search-input'

interface Props {
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
}

interface IconConfig {
  path?: string | ((isOpen: boolean) => string)
  component?: React.ComponentType
  alt: string
  action?: string
}

const ICON_CONFIG: Record<Props['type'], { icons: IconConfig[] }> = {
  DEFAULT: {
    icons: [
      {
        path: (isOpen: boolean) =>
          isOpen ? '/icon/channel/type-group-enable.svg' : '/icon/channel/type-group.svg',
        alt: '유저 리스트',
        action: 'toggleStatusBar'
      }
    ]
  },
  DM: {
    icons: [
      { path: '/icon/dm/call.svg', alt: '음성 통화', action: 'call' },
      { path: '/icon/dm/video.svg', alt: '비디오 통화', action: 'video' }
    ]
  },
  VOICE: {
    icons: [
      {
        component: X,
        alt: '닫기',
        action: 'close'
      }
    ]
  },
  FRIEND: {
    icons: []
  }
} as const

const HeaderToolBar = ({
  type,
  isStatusBarOpen,
  onToggleStatusBar,
  onClose,
  searchProps
}: Props) => {
  const config = ICON_CONFIG[type]

  const handleClick = (action?: string) => {
    if (action === 'toggleStatusBar' && onToggleStatusBar) {
      onToggleStatusBar()
    } else if (action === 'close' && onClose) {
      onClose()
    }
  }

  return (
    <div className='inline-flex items-center'>
      <div className='flex items-center gap-4'>
        {config.icons.map((icon, index) => {
          return (
            <button
              key={index}
              type='button'
              className='cursor-pointer rounded-xl opacity-80 transition-opacity hover:opacity-100'
              onClick={() => handleClick(icon.action)}
              aria-label={icon.alt}>
              {icon.component ? (
                <div className='w-[30px] h-[30px] text-discord-font-color-normal'>
                  <icon.component />
                </div>
              ) : (
                <img
                  className='w-[30px] h-[30px]'
                  src={
                    typeof icon.path === 'function'
                      ? icon.path(isStatusBarOpen ?? false)
                      : icon.path
                  }
                  alt={icon.alt}
                />
              )}
            </button>
          )
        })}

        {type !== 'VOICE' && searchProps && <SearchInput {...searchProps} />}
      </div>
    </div>
  )
}

export default HeaderToolBar
