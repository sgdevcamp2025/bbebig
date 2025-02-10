import { cva } from 'class-variance-authority'
import { CSSProperties } from 'react'

type Props = {
  name: string
  avatarUrl: string
  backgroundUrl?: string
  backgroundColor: CSSProperties['backgroundColor']
  micStatus?: boolean
  headphoneStatus?: boolean
  size: 'sm' | 'md' | 'lg'
}

const cardSize = cva(
  'relative flex items-center justify-center rounded-md overflow-hidden rounded-[8px]',
  {
    variants: {
      size: {
        sm: 'w-[160px] h-[90px]',
        md: 'w-[628px] h-[346px]',
        lg: 'w-[1000px] h-[562px]'
      }
    }
  }
)

const avatarSize = cva('rounded-full', {
  variants: {
    size: {
      sm: 'w-10 h-10',
      md: 'w-20 h-20',
      lg: 'w-20 h-20'
    }
  }
})

function AvatarCard({
  name,
  avatarUrl,
  backgroundUrl,
  backgroundColor,
  size,
  micStatus = true,
  headphoneStatus = true
}: Props) {
  const hasMuteStatus = !micStatus || !headphoneStatus

  return (
    <div
      style={{
        backgroundImage: backgroundUrl ? `url(${backgroundUrl})` : 'none',
        backgroundColor: backgroundColor ? backgroundColor : 'transparent'
      }}
      className={`${cardSize({ size })} group bg-no-repeat bg-cover`}>
      <img
        src={avatarUrl}
        className={avatarSize({ size })}
      />
      <div className='absolute bottom-0 w-full p-2 flex justify-between items-center'>
        {size !== 'sm' ? (
          <div className='flex items-center gap-2 px-3 py-2 rounded-[8px] bg-black/40 group-hover:opacity-100 opacity-0 transition-opacity duration-300'>
            <span className='text-white-100 text-[14px]'>{name}</span>
          </div>
        ) : (
          <div />
        )}
        {hasMuteStatus && (
          <div className='w-8 h-8 rounded-full bg-black/40 flex items-center justify-center'>
            <img
              src={`/icon/channel/${!headphoneStatus ? 'microphone' : 'sound'}-muted.svg`}
              className='w-5 h-5 text-white-100'
            />
          </div>
        )}
      </div>
    </div>
  )
}

export default AvatarCard
