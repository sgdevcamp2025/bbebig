import { useState } from 'react'
import { cn } from '@/libs/cn'
import { Category } from '@/types/channel'

type ServerSideBarProps = {
  serverName?: string
  categories?: Category[]
  selectedChannelId?: string
  onChannelSelect?: (channelId: number) => void
}

function ServerSideBar({
  serverName,
  categories = [],
  selectedChannelId,
  onChannelSelect
}: ServerSideBarProps) {
  const [expandedCategories, setExpandedCategories] = useState<number[]>([])

  const toggleCategory = (categoryId: number) => {
    setExpandedCategories((prev) =>
      prev.includes(categoryId) ? prev.filter((id) => id !== categoryId) : [...prev, categoryId]
    )
  }

  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
      <div className='h-12 px-4 flex items-center justify-between border-b border-discord-gray-800'>
        <h2 className='text-discord-font-color-normal font-semibold'>{serverName}</h2>
        <svg
          width='9'
          height='9'
          viewBox='0 0 9 9'
          fill='none'>
          <path
            fillRule='evenodd'
            clipRule='evenodd'
            d='M0.768198 2.13313C0.592462 1.95562 0.307538 1.95562 0.131802 2.13313C-0.043934 2.31064 -0.043934 2.59845 0.131802 2.77596L4.1818 6.86687C4.35754 7.04438 4.64246 7.04438 4.8182 6.86687L8.8682 2.77596C9.04393 2.59845 9.04393 2.31064 8.8682 2.13313C8.69246 1.95562 8.40754 1.95562 8.2318 2.13313L4.5 5.90263L0.768198 2.13313Z'
            fill='white'
          />
        </svg>
      </div>

      {/* 카테고리 & 채널 목록 */}
      <div className='flex-1 overflow-y-auto'>
        {categories?.map((category) => (
          <div
            key={category.id}
            className='mt-4'>
            <button
              onClick={() => toggleCategory(category.id)}
              className='w-full px-2 flex items-center group'>
              <span
                className={cn(
                  'mr-1.5 transition-transform text-discord-font-color-muted',
                  expandedCategories.includes(category.id) ? 'rotate-0' : '-rotate-90'
                )}>
                <svg
                  width='9'
                  height='9'
                  viewBox='0 0 9 9'
                  fill='none'>
                  <path
                    fillRule='evenodd'
                    clipRule='evenodd'
                    d='M0.768198 2.13313C0.592462 1.95562 0.307538 1.95562 0.131802 2.13313C-0.043934 2.31064 -0.043934 2.59845 0.131802 2.77596L4.1818 6.86687C4.35754 7.04438 4.64246 7.04438 4.8182 6.86687L8.8682 2.77596C9.04393 2.59845 9.04393 2.31064 8.8682 2.13313C8.69246 1.95562 8.40754 1.95562 8.2318 2.13313L4.5 5.90263L0.768198 2.13313Z'
                    fill='currentColor'
                  />
                </svg>
              </span>
              <span className='text-xs font-semibold text-discord-font-color-muted uppercase'>
                {category.name}
              </span>
            </button>

            {expandedCategories.includes(category.id) && (
              <div className='px-2 mt-1'>
                {category.channels.map((channel) => (
                  <button
                    key={channel.id}
                    type='button'
                    onClick={() => onChannelSelect?.(channel.id)}
                    className={cn(
                      'w-full flex items-center px-2 py-1 rounded transition-colors',
                      selectedChannelId && Number(selectedChannelId) === channel.id
                        ? 'bg-discord-gray-500 text-white'
                        : 'text-discord-font-color-muted hover:bg-discord-gray-600'
                    )}>
                    <span className='mr-1'>
                      <img
                        src={`/icon/channel/type-${channel.type.toLocaleLowerCase()}.svg`}
                        width={15}
                        height={15}
                      />
                    </span>
                    <span className='flex-1 ml-1 text-left'>{channel.name}</span>

                    <div
                      className={cn(
                        'flex items-center gap-2 transition-opacity',
                        selectedChannelId && Number(selectedChannelId) === channel.id
                          ? 'opacity-100'
                          : 'opacity-0 group-hover:opacity-100'
                      )}>
                      {channel.type === 'VOICE' && (
                        <img
                          src='/icon/channel/threads.svg'
                          width={15}
                          height={15}
                          alt='스레드'
                        />
                      )}

                      <img
                        src='/icon/channel/invite.svg'
                        width={15}
                        height={15}
                        alt='초대'
                      />
                      <img
                        src='/icon/channel/setting.svg'
                        width={15}
                        height={15}
                        alt='설정'
                      />
                    </div>
                  </button>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}

export default ServerSideBar
