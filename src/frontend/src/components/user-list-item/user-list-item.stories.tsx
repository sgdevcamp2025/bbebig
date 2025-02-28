import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'

import { Inner } from '.'

const meta = {
  title: 'Component/UserListItem',
  component: Inner,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs']
} satisfies Meta<typeof Inner>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryUserListItem: Story = {
  args: {
    id: 1,
    avatarUrl: '/image/common/default-avatar.png',
    name: '이지형',
    status: 'ONLINE',
    description: '오프라인',
    iconType: 'default',
    handleNavigateToDM: () => fn(),
    handleDMIconClick: () => fn()
  },
  render: (args) => {
    return (
      <div className='p-4 h-[400px] w-[300px] bg-discord-gray-700'>
        <Inner {...args} />
      </div>
    )
  }
}
