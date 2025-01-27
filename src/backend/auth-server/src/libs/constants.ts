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

const ERROR_MESSAGE = {
  duplicateEmail: {
    code: 400,
    message: 'Duplicate Email',
  },
  passwordNotMatch: {
    code: 400,
    message: 'Password Not Match',
  },
  badRequest: {
    code: 400,
    message: 'Bad Request',
  },
  unauthorized: {
    code: 401,
    message: 'Unauthorized',
  },
  invalidToken: {
    code: 401,
    message: 'Invalid token',
  },
  notExpired: {
    code: 401,
    message: 'Token Not Expired',
  },
  forbidden: {
    code: 403,
    message: 'Forbidden',
  },
  alreadySignup: {
    code: 403,
    message: 'Already Sign Up',
  },
  notFound: {
    code: 404,
    message: 'Not Found',
  },
  preconditionFailed: {
    code: 412,
    message: 'Precondition Failed',
  },
  verifyTokenFailed: {
    code: 401,
    message: 'Verify Token Failed',
    result: {
      memberId: -1,
      valid: false,
    },
  },
  serverError: {
    code: 500,
    message: 'Server Error',
  },
} as const;

const SUCCESS_MESSAGE = {
  loginOk: {
    code: 200,
    message: 'Login Ok!',
  },
  logoutOk: {
    code: 205,
    message: 'Logout success!',
  },
  refreshToken: {
    code: 201,
    message: 'refresh success',
  },
  accessTokenOk: {
    code: 200,
    message: 'access token ok',
  },
  registerOk: {
    code: 201,
    message: 'register success!',
  },
  verifyTokenOk: {
    code: 200,
    message: 'token verify success!',
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
