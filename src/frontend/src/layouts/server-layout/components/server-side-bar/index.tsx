import { ChevronDown, CirclePlus, FolderPlus, Plus, Settings, UserRoundPlus, X } from 'lucide-react'
import { Suspense, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { useGetServerInfo } from '@/hooks/queries/server/useGetServerInfo'
import useGetSelfUser from '@/hooks/queries/user/useGetSelfUser'
import useClickOutside from '@/hooks/use-click-outside'
import { cn } from '@/libs/cn'

import CategoryCreateModal from '../cateogry-create-modal'
import ChannelCreateModal from '../channel-create-modal'
import SettingModal from '../setting-modal'
import ServerSideBarSkeleton from './server-side-bar-skeleton'

interface ServerSideBarProps {
  serverId: string
  channelId: string
}

export function Inner({ serverId, channelId }: ServerSideBarProps) {
  const serverData = useGetServerInfo(serverId)
  const myInfo = useGetSelfUser()
  const navigate = useNavigate()

  const { serverName, categoryInfoList, channelInfoList } = serverData

  const categories = categoryInfoList.map((category) => ({
    ...category,
    channelInfoList: channelInfoList.filter((channel) => channel.categoryId === category.categoryId)
  }))

  const [expandedCategories, setExpandedCategories] = useState<number[]>(
    categories.map((category) => category.categoryId)
  )

  const [activeChannelId, setActiveChannelId] = useState<number | null>(
    channelId ? Number(channelId) : null
  )

  const [selectedChannel, setSelectedChannel] = useState<{ id: number; name: string } | null>(null)

  const [serverMenuOpen, setServerMenuOpen] = useState(false)
  const [channelCreateModalOpen, setChannelCreateModalOpen] = useState(false)
  const [categoryCreateModalOpen, setCategoryCreateModalOpen] = useState(false)

  const serverMenuRef = useRef<HTMLDivElement>(null)
  const selectCategoryId = useRef<number | null>(null)

  const toggleCategory = (categoryId: number) => {
    setExpandedCategories((prev) =>
      prev.includes(categoryId) ? prev.filter((id) => id !== categoryId) : [...prev, categoryId]
    )
  }

  const handleChannelSelect = (channelId: number) => {
    setActiveChannelId(channelId)
    navigate(`/channels/${serverId}/${channelId}`)
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
    }
  ]

  const AdminMenu = [
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
        selectCategoryId.current = -1
      }
    },
    {
      name: '카테고리 만들기',
      icon: <FolderPlus className='w-4 h-4' />,
      color: 'text-gray-10',
      onClick: () => {
        setCategoryCreateModalOpen(true)
        selectCategoryId.current = -1
      }
    }
  ] as const

  const isAdmin = serverData.ownerId === myInfo.id

  if (!isAdmin) {
    ServerMenu.concat(AdminMenu)
  }

  return (
    <>
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
            key={category.categoryId}
            className='mt-4'>
            <div
              onClick={() => toggleCategory(category.categoryId)}
              className='w-full px-2 flex items-center justify-between group'>
              <div className='flex items-center'>
                <span
                  className={cn(
                    'mr-1.5 transition-transform text-discord-font-color-muted',
                    expandedCategories.includes(category.categoryId) ? 'rotate-0' : '-rotate-90'
                  )}>
                  <ChevronDown className='w-4 h-4 text-discord-font-color-muted' />
                </span>
                <span className='text-xs font-semibold text-discord-font-color-muted uppercase'>
                  {category.categoryName}
                </span>
              </div>
              <button
                type='button'
                onClick={(e) => {
                  e.stopPropagation()
                  selectCategoryId.current = category.categoryId
                  setChannelCreateModalOpen(true)
                }}>
                <Plus className='w-4 h-4 text-discord-font-color-muted' />
              </button>
            </div>

            {expandedCategories.includes(category.categoryId) && (
              <div className='px-2 mt-1'>
                {category.channelInfoList.map((channel) => (
                  <div
                    key={channel.channelId}
                    onClick={() => handleChannelSelect(channel.channelId)}
                    className={cn(
                      'w-full flex items-center px-2 py-1 rounded transition-colors cursor-pointer',
                      activeChannelId === channel.channelId
                        ? 'bg-discord-gray-500 text-white'
                        : 'text-discord-font-color-muted hover:bg-discord-gray-600'
                    )}>
                    <span className='mr-1'>
                      <img
                        src={`/icon/channel/type-${channel.channelType.toLocaleLowerCase()}.svg`}
                        className='w-[15px] h-[15px]'
                      />
                    </span>
                    <span className='flex-1 ml-1 text-left'>{channel.channelName}</span>
                    <div
                      className={cn(
                        'flex items-center gap-2 transition-opacity',
                        selectedChannel && Number(selectedChannel.id) === channel.channelId
                          ? 'opacity-100'
                          : 'opacity-0 group-hover:opacity-100'
                      )}>
                      {channel.channelType === 'VOICE' && (
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
                        onClick={() => handleOpenSettings(channel.channelId, channel.channelName)}>
                        <img
                          src='/icon/channel/setting.svg'
                          className='w-[15px] h-[15px]'
                          alt='설정'
                        />
                      </button>
                    </div>
                  </div>
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
        selectCategoryId={selectCategoryId.current ?? -1}
        isOpen={channelCreateModalOpen}
        onClose={() => setChannelCreateModalOpen(false)}
      />
      <CategoryCreateModal
        isOpen={categoryCreateModalOpen}
        onClose={() => setCategoryCreateModalOpen(false)}
      />
    </>
  )
}

export function ServerSideBar({ serverId, channelId }: { serverId: string; channelId: string }) {
  return (
    <div className='w-60 bg-discord-gray-700 h-[calc(100%-52px)] flex flex-col'>
      <Suspense fallback={<ServerSideBarSkeleton />}>
        <Inner
          serverId={serverId}
          channelId={channelId}
        />
      </Suspense>
    </div>
  )
}

export default ServerSideBar
