import type { Meta, StoryObj } from '@storybook/react'
import { useState } from 'react'

import ServerIcon from '.'

const meta = {
  title: 'Component/ServerIcon',
  component: ServerIcon,
  parameters: {
    layout: ''
  },
  tags: ['autodocs'],
  argTypes: {
    imageUrl: {
      control: 'text'
    },
    label: {
      control: 'text'
    },
    isActive: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof ServerIcon>

export default meta
type Story = StoryObj<typeof meta>

export const ActiveServerIcon: Story = {
  args: {
    imageUrl: 'https://placehold.co/75',
    label: 'Server Name',
    isActive: true,
    hasAlarm: true
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isActive, setIsActive] = useState(false)

    return (
      <div className='w-full h-[500px] flex justify-center items-center'>
        <div className='w-[480px] bg-gray-20 h-[100px] flex items-center justify-center'>
          <ServerIcon
            {...args}
            isActive={isActive}
            onClick={() => setIsActive(!isActive)}
          />
        </div>
      </div>
    )
  }
}

export const DefaultServerIcon: Story = {
  args: {
    imageUrl: 'https://placehold.co/75',
    label: 'Server Name',
    isActive: false,
    hasAlarm: false
  },
  render: (args) => (
    <div className='w-full h-[500px] flex justify-center items-center'>
      <div className='w-[480px] bg-gray-20 h-[100px] flex items-center justify-center'>
        <ServerIcon {...args} />
      </div>
    </div>
  )
}

export const AlarmServerIcon: Story = {
  args: {
    imageUrl: 'https://placehold.co/75',
    label: 'Server Name',
    isActive: false,
    hasAlarm: true
  },
  render: (args) => (
    <div className='w-full h-[500px] flex justify-center items-center'>
      <div className='w-[480px] bg-gray-20 h-[100px] flex items-center justify-center'>
        <ServerIcon {...args} />
      </div>
    </div>
  )
}
