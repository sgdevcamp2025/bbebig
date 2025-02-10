import { ComponentPropsWithoutRef } from 'react'
import { cn } from '@/libs/cn'
import './style.module.css'
type Props = ComponentPropsWithoutRef<'input'> & {
  label?: string
}

function VolumeSlider({ value, className, onChange, label, ...props }: Props) {
  return (
    <div className='relative w-full'>
      <div
        className='absolute z-0 h-[10px] bg-brand top-[50%] translate-y-[-40%] inset-0 rounded-l-full'
        style={{
          width: `${value}%`
        }}
      />
      <input
        aria-label={label}
        type='range'
        value={value}
        className={cn(`w-full`, className)}
        onChange={onChange}
        {...props}
      />
    </div>
  )
}

export default VolumeSlider
