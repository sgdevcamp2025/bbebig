import type { Meta, StoryObj } from '@storybook/react'
import ServerSideBar from '.'

const meta = {
  title: 'Layout/ServerSidebar',
  component: ServerSideBar,
  parameters: {
    layout: 'fullscreen'
  },
  tags: ['autodocs'],
  argTypes: {
    serverName: { control: 'text' }
  }
} satisfies Meta<typeof ServerSideBar>

export default meta
type Story = StoryObj<typeof meta>

export const ServerSidebar: Story = {
  args: {
    serverName: '서버 이름'
  }
}
