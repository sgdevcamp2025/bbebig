import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import CategoryCreateModal from '.'

const meta = {
  title: 'Layout/ServerLayout/CategoryCreateModal',
  component: CategoryCreateModal,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    isOpen: { control: 'boolean' },
    onClose: { action: 'close' }
  }
} satisfies Meta<typeof CategoryCreateModal>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  args: {
    isOpen: false,
    onClose: () => fn(),
    serverId: '1'
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isOpen, setIsOpen] = useState(args.isOpen)
    return (
      <div className='w-[480px]'>
        <button onClick={() => setIsOpen(true)}>Open Modal</button>
        <CategoryCreateModal
          {...args}
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
          serverId='1'
        />
      </div>
    )
  }
}
