import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import Modal from '.'

const meta = {
  title: 'Component/Modal',
  component: Modal,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {
    isOpen: {
      control: 'boolean'
    }
  }
} satisfies Meta<typeof Modal>

export default meta
type Story = StoryObj<typeof meta>

export const Default: Story = {
  args: {
    isOpen: false,
    onClose: fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [isOpen, setIsOpen] = useState(args.isOpen)

    const handleOpenModal = () => {
      setIsOpen(true)
    }

    const handleCloseModal = () => {
      args.onClose?.()
      setIsOpen(false)
    }

    return (
      <div>
        <button onClick={handleOpenModal}>Open Modal</button>
        <Modal
          isOpen={isOpen}
          onClose={handleCloseModal}>
          <div className='bg-white-100 w-[300px] h-[300px]'>Modal</div>
        </Modal>
      </div>
    )
  }
}
