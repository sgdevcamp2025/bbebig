import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import ChannelCreateModal from '.'

const meta = {
  title: 'Layout/ServerLayout/ChannelCreateModal',
  component: ChannelCreateModal,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    isOpen: { control: 'boolean' },
    onClose: { action: 'close' }
  }
} satisfies Meta<typeof ChannelCreateModal>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  args: {
    isOpen: false,
    onClose: () => fn(),
    categoryInfo: {
      id: '1',
      name: '테스트 카테고리 이름'
    }
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isOpen, setIsOpen] = useState(args.isOpen)
    return (
      <div className='w-[480px]'>
        <button onClick={() => setIsOpen(true)}>Open Modal</button>
        <ChannelCreateModal
          {...args}
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
        />
      </div>
    )
  }
}
