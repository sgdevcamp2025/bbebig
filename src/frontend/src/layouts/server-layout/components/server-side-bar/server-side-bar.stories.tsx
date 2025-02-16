import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'

import ServerSideBar from '.'

const meta = {
  title: 'Layout/ServerLayout/ServerSidebar',
  component: ServerSideBar,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {}
} satisfies Meta<typeof ServerSideBar>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryServerSidebar: Story = {
  args: {
    serverId: 1,
    serverName: '서버 이름',
    categories: [
      {
        categoryId: 1,
        categoryName: '카테고리 이름',
        channelInfoList: [
          {
            channelId: 1,
            channelName: '채널 이름',
            channelType: 'TEXT',
            privateStatus: true,
            position: 1
          }
        ],
        position: 1
      },
      {
        categoryId: 2,
        categoryName: '카테고리 이름',
        channelInfoList: [
          {
            channelId: 2,
            channelName: '채널 이름',
            channelType: 'VOICE',
            privateStatus: true,
            position: 1
          }
        ],
        position: 1
      },
      {
        categoryId: 3,
        categoryName: '카테고리 이름',
        channelInfoList: [
          {
            channelId: 3,
            channelName: '채널 이름',
            channelType: 'TEXT',
            privateStatus: true,
            position: 1
          }
        ],
        position: 1
      }
    ],
    selectedChannelId: '1',
    onChannelSelect: () => fn()
  },
  render: (args) => {
    return (
      <div className='p-4 h-[400px]'>
        <ServerSideBar {...args} />
      </div>
    )
  }
}
