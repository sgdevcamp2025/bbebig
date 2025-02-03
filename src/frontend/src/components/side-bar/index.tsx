import { useState } from 'react'
import { cn } from '@/libs/cn'
import { Category } from '@/types/channel'

type SidebarProps = {
  type: 'dm' | 'server'
  serverName?: string
  categories?: Category[]
  selectedChannelId?: string
  onChannelSelect?: (channelId: number) => void
}

function Sidebar({
  type,
  serverName,
  categories = [],
  selectedChannelId,
  onChannelSelect
}: SidebarProps) {
  const [expandedCategories, setExpandedCategories] = useState<number[]>([])

  const toggleCategory = (categoryId: number) => {
    setExpandedCategories((prev) =>
      prev.includes(categoryId) ? prev.filter((id) => id !== categoryId) : [...prev, categoryId]
    )
  }

  if (type === 'dm') {
    return (
      <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
        <div className='h-12 px-4 flex items-center justify-between border-b border-discord-gray-800'>
          <span className='text-discord-font-color-normal font-medium'>개인 대화창</span>
        </div>
      </div>
    )
  }

  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
      <div className='h-12 px-4 flex items-center justify-between border-b border-discord-gray-800'>
        <h2 className='text-discord-font-color-normal font-semibold'>{serverName}</h2>
        <svg
          xmlns='http://www.w3.org/2000/svg'
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
                  xmlns='http://www.w3.org/2000/svg'
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
                    onClick={() => onChannelSelect?.(channel.id)}
                    className={cn(
                      'w-full flex items-center px-2 py-1 rounded transition-colors',
                      selectedChannelId && Number(selectedChannelId) === channel.id
                        ? 'bg-discord-gray-500 text-white'
                        : 'text-discord-font-color-muted hover:bg-discord-gray-600'
                    )}>
                    <span className='mr-1'>
                      <svg
                        xmlns='http://www.w3.org/2000/svg'
                        width='16'
                        height='16'
                        viewBox='0 0 14 15'
                        fill='none'>
                        <path
                          d={
                            channel.type === 'TEXT'
                              ? 'M3.01868 13.4974C2.81203 13.4974 2.65554 13.3101 2.69166 13.1059L3.09404 10.8313H0.832232C0.625894 10.8313 0.469496 10.6446 0.505061 10.4406L0.62131 9.77409C0.649123 9.61466 0.787134 9.49828 0.948481 9.49828H3.32654L4.03067 5.49914H1.76887C1.56253 5.49914 1.40613 5.31236 1.4417 5.10843L1.55794 4.44191C1.58576 4.28244 1.72377 4.16609 1.88512 4.16609H4.26317L4.68616 1.77502C4.7143 1.61592 4.85214 1.5 5.01318 1.5H5.66708C5.87371 1.5 6.03022 1.68734 5.99408 1.8915L5.59173 4.16609H9.5774L10.0004 1.77502C10.0285 1.61592 10.1664 1.5 10.3274 1.5H10.9813C11.188 1.5 11.3445 1.68734 11.3083 1.8915L10.906 4.16609H13.1678C13.3741 4.16609 13.5305 4.35287 13.4949 4.5568L13.3787 5.22333C13.3509 5.38279 13.2129 5.49914 13.0515 5.49914H10.6735L9.96933 9.49828H12.2311C12.4375 9.49828 12.5939 9.68504 12.5583 9.889L12.442 10.5555C12.4143 10.715 12.2762 10.8313 12.1149 10.8313H9.73683L9.31382 13.2224C9.28572 13.3815 9.14788 13.4974 8.98679 13.4974H8.33294C8.12629 13.4974 7.96978 13.3101 8.00592 13.1059L8.40827 10.8313H4.4226L3.99961 13.2224C3.97147 13.3815 3.83362 13.4974 3.67258 13.4974H3.01868ZM5.35953 5.49914L4.65539 9.49828H8.64104L9.34517 5.49914H5.35953Z'
                              : 'M6.91995 1.55165C6.66405 1.44664 6.36984 1.5046 6.17416 1.69963L3.23684 4.90872H1.18421C0.807895 4.90872 0.5 5.21626 0.5 5.59063V9.68209C0.5 10.0571 0.807895 10.364 1.18421 10.364H3.23684L6.17416 13.5745C6.36984 13.7695 6.66405 13.8281 6.91995 13.7224C7.17584 13.6167 7.34211 13.3678 7.34211 13.0917V2.18106C7.34211 1.90625 7.17584 1.65598 6.91995 1.55165ZM8.71053 2.86291V4.22674C10.5969 4.22674 12.1316 5.75696 12.1316 7.63635C12.1316 9.51639 10.5969 11.0459 8.71053 11.0459V12.4098C11.3516 12.4098 13.5 10.2692 13.5 7.63635C13.5 5.00481 11.3516 2.86291 8.71053 2.86291ZM8.71053 5.59057C9.84221 5.59057 10.7632 6.50914 10.7632 7.63635C10.7632 8.76492 9.84221 9.68209 8.71053 9.68209V8.31826C9.08753 8.31826 9.39474 8.01208 9.39474 7.63635C9.39474 7.26061 9.08753 6.95443 8.71053 6.95443V5.59057Z'
                          }
                          fill='#72767D'
                        />
                      </svg>
                    </span>
                    <span className='flex-1 text-left'>{channel.name}</span>
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

export default Sidebar
