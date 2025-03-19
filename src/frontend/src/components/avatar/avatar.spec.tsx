import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import Avatar from './index'

describe('Avatar', () => {
  it('should render without avatar url', () => {
    render(
      <Avatar
        name='John Doe'
        avatarUrl={null}
        statusColor='red'
        size='md'
      />
    )
    const img = screen.getByRole('img')
    expect(img.getAttribute('src')).toBe('/image/common/default-avatar.png')
  })

  it('should render with avatar url', () => {
    render(
      <Avatar
        name='John Doe'
        avatarUrl='/homepage/hero-character.png'
        statusColor='red'
        size='md'
      />
    )
    const img = screen.getByRole('img')
    expect(img.getAttribute('src')).toBe('/homepage/hero-character.png')
  })
})
