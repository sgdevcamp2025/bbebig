import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'

import CheckBox from './index'

describe('CheckBox', () => {
  const onChange = vi.fn()
  it('should render', () => {
    render(
      <CheckBox
        label='Checkbox'
        isChecked={false}
        onChange={onChange}
      />
    )

    const checkBox = screen.getByRole('checkbox')
    expect(checkBox).not.toBeNull()
  })

  it('should render checked', () => {
    render(
      <CheckBox
        label='Checkbox'
        isChecked={true}
        onChange={onChange}
      />
    )

    const checkBox = screen.getByRole('checkbox')
    expect(checkBox).not.toBeNull()
  })

  it('should render label', () => {
    render(
      <CheckBox
        label='Checkbox'
        isChecked={false}
        onChange={onChange}
      />
    )

    const label = screen.getByText('Checkbox')
    expect(label).not.toBeNull()
  })

  it('should call onChange', () => {
    const onChange = vi.fn()
    render(
      <CheckBox
        label='Checkbox'
        isChecked={false}
        onChange={onChange}
      />
    )

    const checkBox = screen.getByRole('checkbox')
    fireEvent.click(checkBox)

    expect(onChange).toHaveBeenCalled()
  })
})
