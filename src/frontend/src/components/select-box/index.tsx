import { CheckIcon, ChevronDownIcon } from 'lucide-react'
import { useRef, useState } from 'react'

import useClickOutside from '@/hooks/use-click-outside'
import { cn } from '@/libs/cn'

interface Option<T> {
  label: string
  value: T
}

interface Props<T> {
  label?: string
  options: Option<T>[]
  value: Option<T> | null
  onChange: (value: Option<T>) => void
  mark?: boolean
  forward?: 'top' | 'bottom'
  className?: string
  required?: boolean
}

const SelectBox = <T,>({
  label,
  options,
  value,
  onChange,
  className,
  mark = false,
  forward = 'top',
  ...props
}: Props<T>) => {
  const [isOpen, setIsOpen] = useState(false)
  const selectRef = useRef<HTMLDivElement>(null)
  const [search, setSearch] = useState('')

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value)
  }

  const handleSelectOption = (option: Option<T>) => {
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
      className={cn(
        'bg-black-80 rounded-[3px] relative',
        isOpen ? (forward === 'bottom' ? 'rounded-b-none' : 'rounded-t-none') : '',
        className
      )}>
      {isOpen ? (
        <div
          className={cn(
            'z-10  absolute rounded-[3px] border-[1px] overflow-scroll overflow-x-hidden pb-0 bg-gray-40 border-black-80 left-0 w-full h-fit max-h-[217px]',
            forward === 'top' ? 'bottom-[44px] rounded-b-none' : 'top-[44px] rounded-t-none'
          )}>
          <ul className='flex flex-col gap-2 overflow-y-auto h-full scrollbar-thin scrollbar-thumb-gray-10 scrollbar-track-black-70'>
            {customOptions.map((option, index) => {
              const isSelected = value?.value === option.value
              return (
                <li
                  key={index}
                  className={cn(
                    `py-2 px-2 bg-gray-40 text-gray-10 focus:text-white-10 hover:bg-gray-30 cursor-pointer ${cn(
                      isSelected
                        ? 'bg-gray-70 flex justify-between items-center text-white-100'
                        : ''
                    )}`
                  )}
                  onClick={() => handleSelectOption(option)}>
                  {option.label}
                  {isSelected && mark ? (
                    <span className='rounded-full bg-brand w-5 h-5 flex items-center justify-center text-white-100'>
                      <CheckIcon className='w-[14px] h-[14px]' />
                    </span>
                  ) : null}
                </li>
              )
            })}
          </ul>
        </div>
      ) : null}
      <div className='py-2 px-2 flex justify-between'>
        <label
          htmlFor={label}
          className='text-gray-10 flex max-w-[calc(100%-8px)] items-center mx-1 h-7'>
          {search ? '' : value ? (value as Option<T>).label : label}
        </label>
        <input
          id={label}
          type='text'
          value={search}
          onChange={handleSearch}
          onFocus={() => setIsOpen(true)}
          className='bg-inherit mx-[2px] absolute left-2 top-[50%] -translate-y-[50%] max-w-[calc(100%-40px)] outline-0 w-full rounded-[3px] text-gray-10 outline-none'
          {...props}
        />
        <button
          role='combobox'
          type='button'
          onClick={() => setIsOpen((prev) => !prev)}
          className={cn(
            'absolute right-2 top-[50%] -translate-y-[50%] transition-transform duration-300',
            isOpen ? 'rotate-180' : ''
          )}>
          <ChevronDownIcon
            className={`w-5 h-5 text-gray-10 ${forward === 'top' ? 'rotate-180' : ''}`}
          />
        </button>
      </div>
    </div>
  )
}

export default SelectBox
