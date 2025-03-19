import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'

import SelectBox from './index'

describe('SelectBox', () => {
  it('should select option when clicked', async () => {
    const options = [
      { label: 'Option 1', value: '1' },
      { label: 'Option 2', value: '2' }
    ]
    const onChange = vi.fn()

    render(
      <SelectBox
        label='Select an option'
        options={options}
        value={null}
        onChange={onChange}
      />
    )

    const selectBox = screen.getByRole('combobox')
    fireEvent.click(selectBox)

    const option1 = screen.getByText('Option 1')
    fireEvent.click(option1)

    expect(onChange).toHaveBeenCalledWith(options[0])
  })

  it('should render label', () => {
    const onChange = vi.fn()
    render(
      <SelectBox
        label='Select an option'
        options={[]}
        value={null}
        onChange={onChange}
      />
    )

    const label = screen.getByText('Select an option')
    expect(label).not.toBeNull()
  })
})
