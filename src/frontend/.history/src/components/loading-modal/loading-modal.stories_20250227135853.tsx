import type { Meta, StoryObj } from '@storybook/react'
import { useState } from 'react'

import LoadingModal from '.'

const meta = {
  title: 'Component/LoadingModal',
  component: LoadingModal,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {}
} satisfies Meta<typeof LoadingModal>

export default meta
type Story = StoryObj<typeof meta>

export const Primary: Story = {
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isOpen, setIsOpen] = useState(false)
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
