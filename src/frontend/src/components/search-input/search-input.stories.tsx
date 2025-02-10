import type { Meta, StoryObj } from '@storybook/react'
import SearchInput from '.'

const meta = {
  title: 'Component/SearchInput',
  component: SearchInput,
  parameters: {
    layout: 'centered'
  },
  tags: ['autodocs']
} satisfies Meta<typeof SearchInput>

export default meta
type Story = StoryObj<typeof meta>

export const PrimarySearchInput: Story = {
  args: {
    placeholder: '검색',
    onSearch: (value) => alert(`검색: ${value}`)
  }
}
