import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import AuthInput from './index'

describe('AuthInput', () => {
  it('should render', () => {
    render(<AuthInput label='Email' />)

    const label = screen.getByText('Email')
    expect(label).not.toBeNull()
  })

  it('should render error message', () => {
    render(
      <AuthInput
        label='Email'
        error='Invalid email'
      />
    )

    const error = screen.getByText('Invalid email')
    expect(error).not.toBeNull()
  })

  it('should render required message', () => {
    render(
      <AuthInput
        label='Email'
        required
      />
    )

    const required = screen.getByText('*')
    expect(required).not.toBeNull()
  })

  it('should render input', () => {
    render(<AuthInput label='Email' />)

    const input = screen.getByRole('textbox')
    expect(input).not.toBeNull()
  })
})
