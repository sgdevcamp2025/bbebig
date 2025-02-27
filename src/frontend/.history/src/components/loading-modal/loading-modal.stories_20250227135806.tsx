import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import LoadingModal from '.'

const meta = {
  title: 'Component/LoadingModal',
  component: LoadingModal,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    isOpen: { control: 'boolean' },
    onClose: { action: 'close' }
  }
} satisfies Meta<typeof LoadingModal>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  args: {
    isOpen: false,
    onClose: () => fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isOpen, setIsOpen] = useState(() => args.isOpen)
    return (
      <div className='w-[480px]'>
        <button onClick={() => setIsOpen(true)}>Open Modal</button>
        <LoadingModal
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
        />
      </div>
    )
  }
}
