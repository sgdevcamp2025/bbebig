export function sum(a: number, b: number) {
  return a + b;
}

describe("sum", () => {
  it("should return 3 when 1 and 2 are passed", () => {
    expect(sum(1, 2)).toBe(3);
  });
});
