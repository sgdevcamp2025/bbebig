import { ChevronDown, CirclePlus, FolderPlus, Plus, Settings, UserRoundPlus, X } from 'lucide-react'
import { useRef, useState } from 'react'

import useClickOutside from '@/hooks/use-click-outside'
import { cn } from '@/libs/cn'
import { Category } from '@/types/server'

import CategoryCreateModal from '../cateogry-create-modal'
import ChannelCreateModal from '../channel-create-modal'
import SettingModal from '../setting-modal'

interface ServerSideBarProps {
  serverId: string
  serverName?: string
  categories?: Category[]
  selectedChannelId?: string
  onChannelSelect?: (channelId: number) => void
}

function ServerSideBar({
  serverName,
  categories = [],
  serverId,
  selectedChannelId,
  onChannelSelect
}: ServerSideBarProps) {
  const [serverMenuOpen, setServerMenuOpen] = useState(false)
  const [expandedCategories, setExpandedCategories] = useState<number[]>([])
  const [selectedChannel, setSelectedChannel] = useState<{ id: number; name: string } | null>(null)
  const [channelCreateModalOpen, setChannelCreateModalOpen] = useState(false)
  const [categoryCreateModalOpen, setCategoryCreateModalOpen] = useState(false)
  const serverMenuRef = useRef<HTMLDivElement>(null)
  const selectCategoryId = useRef<number | null>(null)

  const toggleCategory = (categoryId: number) => {
    setExpandedCategories((prev) =>
      prev.includes(categoryId) ? prev.filter((id) => id !== categoryId) : [...prev, categoryId]
    )
  }

  const handleOpenSettings = (channelId: number, channelName: string) => {
    setSelectedChannel({ id: channelId, name: channelName })
  }

  const handleCloseServerMenu = () => {
    setServerMenuOpen((prev) => !prev)
  }

  useClickOutside(serverMenuRef, () => setServerMenuOpen(false))

  const ServerMenu = [
    {
      name: '초대하기',
      icon: <UserRoundPlus className='w-4 h-4' />,
      color: 'text-brand-20',
      onClick: () => {
        console.log('초대하기')
      }
    },
    {
      name: '서버 설정',
      icon: <Settings className='w-4 h-4' />,
      color: 'text-gray-10',
      onClick: () => {
        console.log('서버 설정')
      }
    },
    {
      name: '채널 만들기',
      icon: <CirclePlus className='w-4 h-4' />,
      color: 'text-gray-10',
      onClick: () => {
        setChannelCreateModalOpen(true)
        selectCategoryId.current = null
      }
    },
    {
      name: '카테고리 만들기',
      icon: <FolderPlus className='w-4 h-4' />,
      color: 'text-gray-10',
      onClick: () => {
        setCategoryCreateModalOpen(true)
        selectCategoryId.current = null
      }
    }
  ] as const

  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
      <button
        type='button'
        onClick={handleCloseServerMenu}
        className='h-12 px-4 flex text-discord-brand items-center justify-between border-b duration-200 border-discord-gray-800 hover:bg-discord-gray-600'>
        <h2 className='text-discord-font-color-normal font-semibold'>{serverName}</h2>
        {serverMenuOpen ? (
          <X className='w-5 h-5 text-white-10' />
        ) : (
          <ChevronDown className='w-5 h-5 text-white-10' />
        )}
      </button>

      {serverMenuOpen && (
        <>
          <div className='fixed inset-0 z-10' />
          <div
            ref={serverMenuRef}
            className='absolute z-10 top-14 left-[10px] w-[220px] bg-[#0F1013] rounded-md'>
            <ul className='flex flex-col p-1 py-2'>
              {ServerMenu.map((menu) => (
                <li
                  key={menu.name}
                  className='group'>
                  <button
                    type='button'
                    onClick={menu.onClick}
                    className='w-full h-8 flex items-center justify-between border-discord-gray-800 group-hover:bg-brand p-2 rounded-md'>
                    <h2
                      className={cn(
                        'text-white-20 text-[14px] leading-[18px] font-medium',
                        menu.color,
                        'group-hover:text-white'
                      )}>
                      {menu.name}
                    </h2>
                    <div
                      className={cn(
                        'w-5 h-5 rounded-full flex items-center justify-center',
                        menu.color,
                        'group-hover:text-white'
                      )}>
                      {menu.icon}
                    </div>
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </>
      )}

      {/* 카테고리 & 채널 목록 */}
      <div className='flex-1 overflow-y-auto'>
        {categories?.map((category) => (
          <div
            key={category.id}
            className='mt-4'>
            <button
              onClick={() => toggleCategory(category.id)}
              className='w-full px-2 flex items-center justify-between group'>
              <div className='flex items-center'>
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
              </div>
              <button
                type='button'
                onClick={(e) => {
                  e.stopPropagation()
                  selectCategoryId.current = category.id
                  setCategoryCreateModalOpen(true)
                }}>
                <Plus className='w-4 h-4 text-discord-font-color-muted' />
              </button>
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
                        className='w-[15px] h-[15px]'
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
                          className='w-[15px] h-[15px]'
                          alt='스레드'
                        />
                      )}

                      <img
                        src='/icon/channel/invite.svg'
                        className='w-[15px] h-[15px]'
                        alt='초대'
                      />

                      <button
                        type='button'
                        onClick={() => handleOpenSettings(channel.id, channel.name)}>
                        <img
                          src='/icon/channel/setting.svg'
                          className='w-[15px] h-[15px]'
                          alt='설정'
                        />
                      </button>
                    </div>
                  </button>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
      <SettingModal
        channelName={selectedChannel?.name ?? ''}
        isOpen={!!selectedChannel}
        onClose={() => setSelectedChannel(null)}
      />
      <ChannelCreateModal
        isOpen={channelCreateModalOpen}
        onClose={() => setChannelCreateModalOpen(false)}
      />
      <CategoryCreateModal
        isOpen={categoryCreateModalOpen}
        onClose={() => setCategoryCreateModalOpen(false)}
        serverId={serverId}
      />
    </div>
  )
}

export default ServerSideBar
