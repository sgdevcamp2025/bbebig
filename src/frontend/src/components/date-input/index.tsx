import { useState } from 'react'

import SelectBox from '../select-box'

interface Props {
  label: string
  required?: boolean
}

const year = Array.from({ length: 100 }, (_, i) => 2025 - i)
const month = Array.from({ length: 12 }, (_, i) => i + 1)
const day = Array.from({ length: 31 }, (_, i) => i + 1)

function DateInput({ label, required }: Props) {
  const [selectedYear, setSelectedYear] = useState<number | null>(null)
  const [selectedMonth, setSelectedMonth] = useState<number | null>(null)
  const [selectedDay, setSelectedDay] = useState<number | null>(null)

  return (
    <div>
      <label className='text-white-20 mb-2 block font-bold text-[12px] leading-[1.33] bold'>
        {label} {required && <span className='text-red-10'>*</span>}
      </label>
      <div className='flex justify-between'>
        <SelectBox
          label='년'
          options={year}
          value={selectedYear}
          onChange={setSelectedYear}
          className='w-[30%]'
        />
        <SelectBox
          label='월'
          prefix='월'
          options={month}
          value={selectedMonth}
          onChange={setSelectedMonth}
          className='w-[35%]'
        />
        <SelectBox
          label='일'
          options={day}
          value={selectedDay}
          onChange={setSelectedDay}
          className='w-[30%]'
        />
      </div>
    </div>
  )
}

export default DateInput
