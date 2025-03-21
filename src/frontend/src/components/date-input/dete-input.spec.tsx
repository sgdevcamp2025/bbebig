import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'

import DateInput from './index'

describe('DateInput', () => {
  const setDate = vi.fn()
  it('should render', () => {
    render(
      <DateInput
        label='Date'
        setDate={setDate}
      />
    )

    const label = screen.getByText('Date')
    expect(label).not.toBeNull()
  })

  it('should call setDate', () => {
    render(
      <DateInput
        label='Date'
        setDate={setDate}
      />
    )

    const selectBoxYear = screen.getAllByRole('combobox')[0]
    fireEvent.click(selectBoxYear)

    const option1 = screen.getByText('2025')
    fireEvent.click(option1)

    const selectBoxMonth = screen.getAllByRole('combobox')[1]
    fireEvent.click(selectBoxMonth)

    const option2 = screen.getByText('1월')
    fireEvent.click(option2)

    const selectBoxDay = screen.getAllByRole('combobox')[2]
    fireEvent.click(selectBoxDay)

    const option3 = screen.getByText('30')
    fireEvent.click(option3)

    expect(setDate).toHaveBeenCalledWith('2025-01-30')
  })

  it('should render error', () => {
    render(
      <DateInput
        label='Date'
        setDate={setDate}
        error='Error'
      />
    )

    const error = screen.getByText('Error')
    expect(error).not.toBeNull()
  })

  it('should render required', () => {
    render(
      <DateInput
        label='Date'
        setDate={setDate}
        required
      />
    )

    const required = screen.getByText('*')
    expect(required).not.toBeNull()
  })
})
