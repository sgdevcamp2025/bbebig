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
    isSuccess: false,
    code: 400,
    message: 'Duplicate Email',
  },
  passwordNotMatch: {
    isSuccess: false,
    code: 400,
    message: 'Password Not Match',
  },
  badRequest: {
    isSuccess: false,
    code: 400,
    message: 'Bad Request',
  },
  unauthorized: {
    isSuccess: false,
    code: 401,
    message: 'Unauthorized',
  },
  invalidToken: {
    isSuccess: false,
    code: 401,
    message: 'Invalid token',
  },
  notExpired: {
    isSuccess: false,
    code: 401,
    message: 'Token Not Expired',
  },
  forbidden: {
    isSuccess: false,
    code: 403,
    message: 'Forbidden',
  },
  alreadySignup: {
    isSuccess: false,
    code: 403,
    message: 'Already Sign Up',
  },
  notFound: {
    isSuccess: false,
    code: 404,
    message: 'Not Found',
  },
  preconditionFailed: {
    isSuccess: false,
    code: 412,
    message: 'Precondition Failed',
  },
  serverError: {
    isSuccess: false,
    code: 500,
    message: 'Server Error',
  },
} as const;

const SUCCESS_MESSAGE = {
  loginOk: {
    isSuccess: true,
    code: 200,
    message: 'Login Ok!',
  },
  logoutOk: {
    isSuccess: true,
    code: 205,
    message: 'Logout success!',
  },
  refreshToken: {
    isSuccess: true,
    code: 201,
    message: 'refresh success',
  },
  accessTokenOk: {
    isSuccess: true,
    code: 200,
    message: 'access token ok',
  },
  registerOk: {
    isSuccess: true,
    code: 201,
    message: 'register success!',
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
