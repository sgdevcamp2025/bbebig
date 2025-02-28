interface Props {
  label: string
  isActive: boolean
  onClick: () => void
}

function MenuButton({ label, isActive, onClick }: Props) {
  const getIconPath = (label: string) => {
    switch (label) {
      case '친구':
        return '/icon/dm/friend.svg'
      case 'Nitro':
        return '/icon/dm/nitro.svg'
      case '상점':
        return '/icon/dm/shop.svg'
      default:
        return ''
    }
  }

  return (
    <button
      onClick={onClick}
      className={`flex items-center gap-3 px-2 py-2 rounded ${
        isActive
          ? 'bg-discord-gray-500 text-white'
          : 'text-discord-gray-300 hover:bg-discord-gray-600 hover:text-discord-font-color-normal'
      }`}>
      <div className='w-6 h-6'>
        <img
          src={getIconPath(label)}
          alt={`${label} 아이콘`}
        />
      </div>
      <div className='text-md font-medium'>{label}</div>
    </button>
  )
}

export default MenuButton
