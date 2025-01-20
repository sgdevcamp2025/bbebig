import type { Meta, StoryObj } from '@storybook/react'

import Button from '.'
import CustomButton from '.'
import { CustomButtonVariants } from './variants'

const meta = {
  title: 'Component/Button',
  component: Button,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    variant: {
      control: 'select',
      options: Object.values(CustomButtonVariants)
    }
  }
} satisfies Meta<typeof CustomButton>

export default meta
type Story = StoryObj<typeof meta>

export const GrayBadge: Story = {
  args: {
    variant: CustomButtonVariants.primary,
    children: 'click me!'
  }
}
