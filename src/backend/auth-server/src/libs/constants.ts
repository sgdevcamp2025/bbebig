import { Secret } from 'jsonwebtoken';

const SERVER_PORT = Number(process.env.SERVER_PORT) || 8083;
const SERVER_URL = process.env.SERVER_URL;
const ROUND = Number(process.env.HASH_ROUND) || 10;
const SECRET_KEY = process.env.SECRET_KEY as Secret;
const ACCESS_TOKEN_EXPIRES = process.env.ACCESS_TOKEN_EXPIRES;
const REFRESH_TOKEN_EXPIRES = process.env.REFRESH_TOKEN_EXPIRES;
const REDIS_HOST = process.env.REDIS_HOST;
const REDIS_PORT = process.env.REDIS_PORT;
const REDIS_BINDING_PORT = process.env.REDIS_BINDING_PORT;
const REDIS_CONTAINER_NAME = process.env.REDIS_CONTAINER_NAME;
const REDIS_IMAGE = process.env.REDIS_IMAGE;
const REDIS_VOLUME = process.env.REDIS_VOLUME;

const AUTH_PREFIX = 'AUTH_';

const ERROR_MESSAGE = {
  badRequest: {
    code: `${AUTH_PREFIX}001`,
    message: 'Bad Request',
  },
  duplicateEmail: {
    code: `${AUTH_PREFIX}002`,
    message: 'Duplicate Email',
  },
  passwordNotMatch: {
    code: `${AUTH_PREFIX}003`,
    message: 'Password Not Match',
  },
  unauthorized: {
    code: `${AUTH_PREFIX}004`,
    message: 'Unauthorized',
  },
  invalidToken: {
    code: `${AUTH_PREFIX}005`,
    message: 'Invalid token',
  },
  notExpired: {
    code: `${AUTH_PREFIX}006`,
    message: 'Token Not Expired',
  },
  forbidden: {
    code: `${AUTH_PREFIX}007`,
    message: 'Forbidden',
  },
  alreadySignup: {
    code: `${AUTH_PREFIX}008`,
    message: 'Already Sign Up',
  },
  notFound: {
    code: `${AUTH_PREFIX}009`,
    message: 'Not Found',
  },
  preconditionFailed: {
    code: `${AUTH_PREFIX}010`,
    message: 'Precondition Failed',
  },
  verifyTokenFailed: {
    code: `${AUTH_PREFIX}011`,
    message: 'Verify Token Failed',
  },
  serverError: {
    code: `${AUTH_PREFIX}012`,
    message: 'Server Error',
  },
} as const;

const SUCCESS_MESSAGE = {
  loginOk: {
    code: `${AUTH_PREFIX}100`,
    message: 'Login Ok!',
  },
  logoutOk: {
    code: `${AUTH_PREFIX}101`,
    message: 'Logout success!',
  },
  refreshToken: {
    code: `${AUTH_PREFIX}102`,
    message: 'refresh success',
  },
  accessTokenOk: {
    code: `${AUTH_PREFIX}103`,
    message: 'access token ok',
  },
  registerOk: {
    code: `${AUTH_PREFIX}104`,
    message: 'register success!',
  },
  verifyTokenOk: {
    code: `${AUTH_PREFIX}105`,
    message: 'token verify success!',
  },
  verifyEmailOk: {
    code: `${AUTH_PREFIX}106`,
    message: 'email verify success!',
  },
  tokenDecodeOk: {
    code: `${AUTH_PREFIX}107`,
    message: 'token decode success!',
  },
} as const;

const REDIS_KEY = {
  refreshToken: (userId: number) => `refreshToken:${userId}`,
} as const;

export {
  SERVER_PORT,
  SERVER_URL,
  ROUND,
  SECRET_KEY,
  ACCESS_TOKEN_EXPIRES,
  REFRESH_TOKEN_EXPIRES,
  ERROR_MESSAGE,
  SUCCESS_MESSAGE,
  REDIS_KEY,
  REDIS_HOST,
  REDIS_PORT,
  REDIS_BINDING_PORT,
  REDIS_CONTAINER_NAME,
  REDIS_IMAGE,
  REDIS_VOLUME,
};
