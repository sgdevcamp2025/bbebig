import { ChevronDownIcon } from 'lucide-react'
import { Dispatch, ReactNode, SetStateAction, useRef, useState } from 'react'

import { cn } from '@/utils/cn'
import useClickOutside from '@/hooks/use-click-outside'

interface Props {
  label: string
  options: string[]
  value: string | null
  onChange: Dispatch<SetStateAction<string | null>>
  className?: string
  required?: boolean
  prefix?: string
}

function SelectBox({ label, options, value, onChange, className, prefix = '', ...props }: Props) {
  const [isOpen, setIsOpen] = useState(false)
  const selectRef = useRef<HTMLDivElement>(null)
  const [search, setSearch] = useState('')

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value)
  }

  const handleSelectOption = (option: string) => {
    onChange(option)
    setSearch('')
    setIsOpen(false)
  }

  useClickOutside(selectRef, () => setIsOpen(false))

  const customOptions = search
    ? options.filter((option) => String(option).startsWith(search))
    : options

  return (
    <div
      ref={selectRef}
      className={cn('bg-black-80 rounded-[3px] relative', className)}>
      {isOpen ? (
        <div className='absolute top-[-217px] rounded-[3px] border-[1px] bg-gray-40 border-black-80 left-0 w-full h-[217px]'>
          <ul className='flex flex-col gap-2 overflow-y-auto h-full scrollbar-thin scrollbar-thumb-gray-10 scrollbar-track-black-70'>
            {customOptions.map((option, index) => {
              const isSelected = value === option
              return (
                <li
                  key={index}
                  className={cn(
                    `py-2 px-2 bg-gray-40 text-gray-10 focus:text-white-100 hover:bg-gray-30 cursor-pointer ${cn(
                      isSelected ? 'bg-gray-70' : ''
                    )}`
                  )}
                  onClick={() => handleSelectOption(option)}>
                  {option as ReactNode}
                </li>
              )
            })}
          </ul>
        </div>
      ) : null}
      <div className='py-2 px-2 flex justify-between'>
        <div className='text-gray-10 flex max-w-[calc(100%-8px)] items-center mx-1 h-7'>
          {search ? '' : value ? (value as string) + prefix : label}
        </div>
        <input
          type='text'
          value={search}
          onChange={handleSearch}
          onFocus={() => setIsOpen(true)}
          className='bg-inherit mx-[2px] absolute left-2 top-[50%] -translate-y-[50%] max-w-[calc(100%-40px)] outline-0 w-full rounded-[3px] text-gray-10 outline-none'
          {...props}
        />
        <button
          type='button'
          onClick={() => setIsOpen((prev) => !prev)}>
          <ChevronDownIcon className='w-5 h-5 text-gray-10' />
        </button>
      </div>
    </div>
  )
}

export default SelectBox
