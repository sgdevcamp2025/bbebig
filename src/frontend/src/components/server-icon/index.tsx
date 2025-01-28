import { ComponentPropsWithoutRef, useState } from 'react'

import { cn } from '@/utils/cn'

type Props = {
  imageUrl: string | null
  imageSize?: number
  label: string
  isActive: boolean
  hasAlarm?: boolean
} & ComponentPropsWithoutRef<'button'>

function ServerIcon({ imageUrl, imageSize = 48, label, isActive, hasAlarm, ...props }: Props) {
  const [isFocused, setIsFocused] = useState(false)

  const handleMouseEnter = () => setIsFocused(true)
  const handleMouseLeave = () => setIsFocused(false)

  return (
    <button
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      type='button'
      className='flex items-center w-full justify-center relative'
      {...props}>
      <div
        className={cn(
          'absolute top-1/2 translate-y-[-50%] left-[-4px] w-2 h-10 rounded-r-[4px] overflow-hidden transition-all duration-300 bg-white',
          !isActive && (isFocused ? 'h-5' : hasAlarm ? 'h-2' : 'h-0')
        )}
      />
      <div className='flex items-center justify-center'>
        <div
          className={cn(
            'w-[48px] h-[48px] flex items-center justify-center bg-brand-10 overflow-hidden transition-all duration-300 hover:rounded-[14px]',
            isActive
              ? 'rounded-[14px] bg-brand text-text-normal'
              : 'rounded-[48px] text-text-white',
            isFocused ? 'rounded-[14px]' : ''
          )}>
          {imageUrl ? (
            <img
              src={imageUrl}
              alt={label}
              width={imageSize}
              height={imageSize}
              className='aspect-square object-cover text-inherit'
              style={{
                fill: 'white'
              }}
            />
          ) : (
            <span className='text-gray-100 text-[14px] font-bold'>{label.charAt(0)}</span>
          )}
        </div>
      </div>
    </button>
  )
}

export default ServerIcon
