import type { Meta, StoryObj } from '@storybook/react'

import HeaderToolBar from '.'

const meta = {
  title: 'Component/HeaderToolBar',
  component: HeaderToolBar,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    type: {
      control: 'select',
      options: ['DEFAULT', 'VOICE', 'DM']
    },
    isStatusBarOpen: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof HeaderToolBar>

export default meta
type Story = StoryObj<typeof meta>

export const PrimaryHeaderToolBar: Story = {
  args: {
    type: 'DEFAULT',
    isStatusBarOpen: false,
    onToggleStatusBar: () => console.log('Status bar toggled'),
    searchProps: {
      value: '검색',
      onChange: () => console.log('검색'),
      handleClear: () => console.log('검색 초기화'),
      placeholder: '검색'
    }
  }
}

export const VoiceChannelHeaderToolBar: Story = {
  args: {
    type: 'VOICE',
    onClose: () => console.log('Voice channel closed')
  }
}

export const DirectMessageHeaderToolBar: Story = {
  args: {
    type: 'DM',
    searchProps: {
      value: '검색',
      onChange: () => console.log('검색'),
      handleClear: () => console.log('검색 초기화'),
      placeholder: '검색'
    }
  }
}

export const StatusBarOpenHeaderToolBar: Story = {
  args: {
    type: 'DEFAULT',
    isStatusBarOpen: true,
    onToggleStatusBar: () => console.log('Status bar toggled'),
    searchProps: {
      value: '검색',
      onChange: () => console.log('검색'),
      handleClear: () => console.log('검색 초기화'),
      placeholder: '검색'
    }
  }
}
