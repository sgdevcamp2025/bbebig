import type { Meta, StoryObj } from '@storybook/react'
import { fn } from '@storybook/test'
import { useState } from 'react'

import Slider from '.'

const meta = {
  title: 'Component/Slider',
  component: Slider,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs'],
  argTypes: {}
} satisfies Meta<typeof Slider>

export default meta
type Story = StoryObj<typeof meta>

export const PrimarySlider: Story = {
  args: {
    value: 0,
    onChange: fn()
  },
  render: (args) => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [value, setValue] = useState<number>(() => args.value as number)

    return (
      <div className='w-full p-5 h-[200px] flex items-center bg-discord-brand-hover'>
        <div className='w-[480px]'>
          <Slider
            {...args}
            value={value}
            onChange={(e) => setValue(Number(e.target.value))}
          />
        </div>
      </div>
    )
  }
}
