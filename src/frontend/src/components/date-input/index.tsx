import { useEffect, useState } from 'react'

import SelectBox from '../select-box'

interface Props {
  label: string
  required?: boolean
  setDate: (value: string) => void
  error?: string
}

const year = Array.from({ length: 100 }, (_, i) => String(2025 - i))
const month = Array.from({ length: 12 }, (_, i) => String(i + 1) + '월')
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
          options={year.map((year) => ({ label: year, value: year + '년' }))}
          value={selectedYear ? { label: selectedYear, value: selectedYear + '년' } : null}
          onChange={(value) => setSelectedYear(value.value)}
          className='w-[30%]'
        />
        <SelectBox
          label='월'
          options={month.map((month) => ({ label: month, value: month + '월' }))}
          value={selectedMonth ? { label: selectedMonth, value: selectedMonth + '월' } : null}
          onChange={(value) => setSelectedMonth(value.value)}
          className='w-[35%]'
        />
        <SelectBox
          label='일'
          options={day.map((day) => ({ label: day, value: day + '일' }))}
          value={selectedDay ? { label: selectedDay, value: selectedDay + '일' } : null}
          onChange={(value) => setSelectedDay(value.value)}
          className='w-[30%]'
        />
      </div>
    </div>
  )
}

export default DateInput
