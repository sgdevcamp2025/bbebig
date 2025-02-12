import { cn } from '@/libs/cn'

interface RadioItem {
  id: string
  label: string
  description?: string
  value: string
  icon?: React.ReactNode
}

interface Props {
  label: string
  items: RadioItem[]
  selectedItem: RadioItem
  onChange: (item: RadioItem) => void
}

function CustomRadio({ label, items, selectedItem, onChange }: Props) {
  return (
    <div className='flex flex-col gap-2'>
      <label className='text-gray-10 text-[12px] leading-[20px] font-bold'>{label}</label>
      <ul className='flex flex-col gap-2'>
        {items.map((item) => (
          <li key={item.id}>
            <button
              type='button'
              className={cn(
                'bg-gray-20 w-full px-[10px] h-full py-[12px] rounded-[3px] flex items-center justify-between',
                selectedItem.id === item.id && 'bg-gray-80'
              )}
              onClick={() => onChange(item)}>
              <div className='flex items-center'>
                <div className='text-[#c8d1db]'>{item.icon}</div>
                <div className='flex flex-col gap-1 ml-3'>
                  <span className='text-text-normal text-left leading-[20px] font-medium'>
                    {item.label}
                  </span>
                  {item.description && (
                    <span className='text-[14px] text-white-20 leading-[16px]'>
                      {item.description}
                    </span>
                  )}
                </div>
              </div>
              <div
                className={cn(
                  'w-5 h-5 border-2 flex items-center justify-center border-white-20 rounded-full',
                  selectedItem.id === item.id && 'border-white-100'
                )}>
                {selectedItem.id === item.id && (
                  <div className='w-2 h-2 rounded-full bg-white-100' />
                )}
              </div>
            </button>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default CustomRadio
