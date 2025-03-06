import { ComponentPropsWithoutRef } from 'react'

type Props = {
  src: string
} & ComponentPropsWithoutRef<'img'>

export function Picture({ src, className, ...props }: Props) {
  return (
    <picture className={className}>
      <source
        srcSet={`${src}.webp`}
        type='image/webp'
      />
      <source
        srcSet={`${src}.avif`}
        type='image/avif'
      />
      <img
        className='w-full h-full'
        src={`${src}.png`}
        alt='test'
        {...props}
      />
    </picture>
  )
}
