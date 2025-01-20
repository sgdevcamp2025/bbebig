import { expect, test } from 'vitest'

const sum = (a: number, b: number) => a + b

test('sum', () => {
  expect(sum(1, 2)).toBe(3)
})
