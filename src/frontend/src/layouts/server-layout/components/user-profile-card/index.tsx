import { useState } from 'react'
import { useNavigate } from 'react-router'

import Avatar from '@/components/avatar'
import { User } from '@/types/user'

interface UserProfileCardProps {
  user: User | null
  onSendFriendRequest: () => void
  onMoreButtonClick: () => void
}

function UserProfileCard({ user, onSendFriendRequest, onMoreButtonClick }: UserProfileCardProps) {
  const [message, setMessage] = useState('')
  const navigate = useNavigate()

  if (!user) return null

  const sendMessage = () => {
    navigate(`/channels/@me`, {
      state: { initialMessage: message }
    })
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
      e.preventDefault()
      sendMessage()
    }
  }

  return (
    <div className='w-[308px] bg-black-100 rounded-[8px] border-black-90 border-[4px]'>
      <div className='relative'>
        {user.bannerUrl ? (
          <img
            src={user.bannerUrl}
            className='w-full h-[105px] object-cover'
          />
        ) : (
          <div className='w-full h-[105px] bg-black-92' />
        )}
      </div>

      <div className='absolute top-2 right-2 flex gap-2'>
        <button
          className='w-8 h-8 bg-black-90 hover:bg-black-80 rounded-full flex items-center justify-center'
          aria-label='친구 요청 보내기'
          onClick={onSendFriendRequest}>
          <img
            src='/icon/channel/invite.svg'
            alt='친구 요청'
            className='w-4 h-4'
          />
        </button>

        <button
          className='w-8 h-8 bg-black-90 hover:bg-black-80 rounded-full flex items-center justify-center'
          onClick={onMoreButtonClick}
          aria-label='더보기'>
          <img
            src='/icon/friend/more.svg'
            alt='더보기'
            className='w-4 h-4'
          />
        </button>
      </div>
      <div className='relative p-4'>
        <div className='flex justify-center items-center absolute top-[-40px]'>
          <Avatar
            status={user.customPresenceStatus}
            avatarUrl={user.avatarUrl}
            size='lg'
            statusColor='black'
            defaultBackgroundColor='black'
          />
        </div>

        <div className='flex flex-col mt-8'>
          <span className='text-lg font-semibold text-white'>{user.name}</span>
          <div className='mt-3 h-8 flex items-center bg-discord-gray-600 rounded-md justify-between px-2'>
            <input
              type='text'
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder={`@${user.name} 님에게 메시지 보내기`}
              className='w-full bg-transparent text-xs text-white outline-none focus-none'
            />
            <img
              alt='emoji-icon'
              src={`/icon/chat/emoji.svg`}
              className='w-4 h-4'
            />
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserProfileCard
