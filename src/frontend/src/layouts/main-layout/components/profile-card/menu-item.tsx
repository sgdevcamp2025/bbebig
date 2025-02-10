import { ChevronRightIcon } from 'lucide-react'

interface Props {
  onClick: () => void
  icon: React.ReactNode
  text: string
  hasChevron?: boolean
}

function MenuItem({ icon, onClick, text, hasChevron }: Props) {
  return (
    <li className='flex items-center hover:bg-gray-80 rounded-md'>
      <button
        type='button'
        onClick={onClick}
        className='w-full p-2'>
        <div className='flex items-center justify-between'>
          <div className='flex gap-1 items-center'>
            <div className='w-4 h-4 flex items-center justify-center'>{icon}</div>
            <p className='text-gray-50 text-[14px]'>{text}</p>
          </div>
          {hasChevron && <ChevronRightIcon className='w-[14px] h-[14px] text-gray-50' />}
        </div>
      </button>
    </li>
  )
}

export default MenuItem
