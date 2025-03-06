interface PictureProps {
  src: string
  className?: string
}

export function Picture({ src, className }: PictureProps) {
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
        src={`${src}.png`}
        alt='test'
      />
    </picture>
  )
}
