import type { Meta, StoryObj } from '@storybook/react'

import AvatarCard from '.'

const meta = {
  title: 'Component/AvatarCard',
  component: AvatarCard,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    name: {
      control: 'text'
    },
    avatarUrl: {
      control: 'text'
    },
    backgroundUrl: {
      control: 'text'
    },
    backgroundColor: {
      control: 'color'
    },
    size: {
      control: 'select',
      options: ['sm', 'md', 'lg']
    },
    micStatus: {
      control: 'boolean'
    },
    headphoneStatus: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof AvatarCard>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryAvatar: Story = {
  args: {
    name: '홍길동',
    avatarUrl: '/image/common/default-avatar.png',
    backgroundUrl: '/image/common/default-background.png',
    backgroundColor: '#fff',
    size: 'lg',
    micStatus: true,
    headphoneStatus: true
  }
}
