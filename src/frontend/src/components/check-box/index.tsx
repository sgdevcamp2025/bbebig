import { CheckIcon } from 'lucide-react'

interface Props {
  isChecked: boolean
  onChange: (isChecked: boolean) => void
  label: string
}

function CheckBox({ isChecked, onChange, label }: Props) {
  return (
    <label className='flex items-center gap-2'>
      <div className='rounded-[6px] w-6 h-6 border border-white-20 relative overflow-hidden'>
        <input
          type='checkbox'
          className='absolute w-full h-full opacity-0 z-[1]'
          checked={isChecked}
          onChange={() => onChange(!isChecked)}
        />
        {isChecked ? (
          <div className=' w-full h-full bg-brand flex items-center justify-center'>
            <CheckIcon className='w-[18px] h-[18px] text-white-100' />
          </div>
        ) : null}
      </div>
      <span className='text-gray-60 text-[12px] leading-[1.33] flex-1'>{label}</span>
    </label>
  )
}

export default CheckBox
