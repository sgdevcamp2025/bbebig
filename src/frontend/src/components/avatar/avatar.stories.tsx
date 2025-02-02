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
    customPresenceStatus: {
      control: 'select',
      options: ['ONLINE', 'OFFLINE', 'NOT_DISTURB', 'INVISIBLE']
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
    statusColor: 'black',
    avatarUrl: '/image/common/default-avatar.png',
    customPresenceStatus: 'ONLINE',
    size: 'lg'
  }
}
