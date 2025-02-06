import type { Meta, StoryObj } from '@storybook/react'

import UserListItem from '.'

const meta = {
  title: 'Component/UserListItem',
  component: UserListItem,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs']
} satisfies Meta<typeof UserListItem>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryUserListItem: Story = {
  args: {
    avatarUrl: '/image/common/default-avatar.png',
    name: '이지형',
    status: 'ONLINE',
    description: '오프라인',
    size: 'sm',
    iconType: 'default'
  }
}
