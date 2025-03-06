import { ComponentPropsWithoutRef } from 'react'

type Props = {
  src: string
} & ComponentPropsWithoutRef<'img'>

export function Picture({ src, children, ...props }: Props) {
  return (
    <picture>
      {children}
      <source
        srcSet={`${src}.webp`}
        type='image/webp'
      />
      <source
        srcSet={`${src}.avif`}
        type='image/avif'
      />
      <img
        src={`${src}.png`}
        alt='test'
        {...props}
      />
    </picture>
  )
}
