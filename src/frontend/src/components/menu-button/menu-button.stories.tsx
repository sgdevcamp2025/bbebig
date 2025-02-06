import type { Meta, StoryObj } from '@storybook/react'

import MenuButton from '.'

const meta = {
  title: 'Component/MenuButton',
  component: MenuButton,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs']
} satisfies Meta<typeof MenuButton>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryMenuButton: Story = {
  args: {
    label: '친구',
    isActive: false,
    onClick: () => {}
  }
}
