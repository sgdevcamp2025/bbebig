import type { Meta, StoryObj } from '@storybook/react'

import Avatar from '.'

const meta = {
  title: 'Component/Avatar',
  component: Avatar,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    avatarUrl: {
      control: 'text'
    },
    status: {
      control: 'select',
      options: ['ONLINE', 'OFFLINE', 'DND', 'INVISIBLE', 'AWAY']
    },
    size: {
      control: 'select',
      options: ['sm', 'lg']
    }
  }
} satisfies Meta<typeof Avatar>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryAvatar: Story = {
  args: {
    name: 'John Doe',
    statusColor: 'black',
    avatarUrl: '/image/common/default-avatar.png',
    status: 'ONLINE',
    size: 'lg'
  }
}
