import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

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
      control: 'text'
    },
    size: {
      control: 'text'
    }
  }
} satisfies Meta<typeof Avatar>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryAvatar: Story = {
  args: {
    avatarUrl: '/images/common/default-avatar.png',
    customPresenceStatus: 'ONLINE',
    size: 'md'
  }
}
