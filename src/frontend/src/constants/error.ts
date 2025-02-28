const ERROR_MESSAGE = {
  TOKEN_EXPIRED: '토큰이 만료되었습니다.',
  TOKEN_INVALID: '토큰이 유효하지 않습니다.',
  API_ERROR: 'API 요청 중 에러가 발생했습니다.'
} as const

const ERROR_CODE = {
  TOKEN_EXPIRED: 401,
  TOKEN_INVALID: 401,
  API_ERROR: 500
} as const

export { ERROR_CODE, ERROR_MESSAGE }
