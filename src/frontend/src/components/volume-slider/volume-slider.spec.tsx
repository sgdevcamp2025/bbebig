import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import VolumeSlider from './index'

describe('VolumeSlider', () => {
  it('should render', () => {
    render(<VolumeSlider />)

    const volumeSlider = screen.getByRole('slider')
    expect(volumeSlider).not.toBeNull()
  })

  it('should render label', () => {
    render(<VolumeSlider label='Volume' />)

    const label = screen.getByLabelText('Volume')
    expect(label).not.toBeNull()
  })
})
