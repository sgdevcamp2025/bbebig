import { useEffect, useState } from 'react'

import SelectBox from '../select-box'

interface Props {
  label: string
  required?: boolean
  setDate: (value: string) => void
  error?: string
}

const year = Array.from({ length: 100 }, (_, i) => String(2025 - i))
const month = Array.from({ length: 12 }, (_, i) => String(i + 1))
const day = Array.from({ length: 31 }, (_, i) => String(i + 1))

function DateInput({ label, required, setDate, error }: Props) {
  const [selectedYear, setSelectedYear] = useState<string | null>(null)
  const [selectedMonth, setSelectedMonth] = useState<string | null>(null)
  const [selectedDay, setSelectedDay] = useState<string | null>(null)

  useEffect(
    function setBirthdateValue() {
      setDate(
        `${selectedYear}-${String(selectedMonth).padStart(2, '0')}-${String(selectedDay).padStart(2, '0')}`
      )
    },
    [selectedYear, selectedMonth, selectedDay, setDate]
  )

  return (
    <div>
      <label className='text-white-20 mb-2 block font-bold text-[12px] leading-[1.33] bold'>
        {label} {required && <span className='text-red-10'>*</span>}
        {error && <span className='text-red-10'>{error}</span>}
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
