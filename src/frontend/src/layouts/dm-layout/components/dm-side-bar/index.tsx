import MenuButton from '@/components/menu-button'
import { useState } from 'react'

function DmSideBar() {
  const [selectedMenu, setSelectedMenu] = useState<string>('친구')
  const handleMenuClick = (label: string) => {
    setSelectedMenu(label)
  }

  return (
    <div className='w-60 bg-discord-gray-700 h-screen flex flex-col'>
      <div className='px-3 py-3 flex items-center justify-between border-b border-discord-gray-800'>
        <input
          type='text'
          placeholder='대화 찾기 또는 시작하기'
          className='w-full h-8 bg-discord-gray-800 p-2 focus:outline-none text-discord-font-color-normal text-sm'
        />
      </div>
      <div className='flex flex-col gap-2 mt-1 mx-2'>
        {['친구', 'Nitro', '상점'].map((label) => (
          <MenuButton
            key={label}
            label={label}
            isActive={selectedMenu === label}
            onClick={() => handleMenuClick(label)}
          />
        ))}
      </div>
    </div>
  )
}

export default DmSideBar
