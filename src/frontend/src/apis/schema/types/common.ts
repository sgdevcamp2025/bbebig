export interface CommonResponseType<T> {
  code: string
  message: string
  result: T
}
