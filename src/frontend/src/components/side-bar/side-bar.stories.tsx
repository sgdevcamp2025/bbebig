import type { Meta, StoryObj } from '@storybook/react'

import Sidebar from '.'

const meta = {
  title: 'Component/SideBar',
  component: Sidebar,
  parameters: {
    layout: 'fullscreen'
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      control: 'radio',
      options: ['dm', 'server']
    },
    serverName: { control: 'text' },
    selectedChannelId: { control: 'text' }
  }
} satisfies Meta<typeof Sidebar>

export default meta
type Story = StoryObj<typeof meta>

export const ServerSidebar: Story = {
  args: {
    type: 'server',
    serverName: '서버 이름',
    selectedChannelId: '1'
  }
}

export const DmSidebar: Story = {
  args: {
    type: 'dm'
  }
}
