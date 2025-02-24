import { Secret } from 'jsonwebtoken';
import { CommonResponseType } from 'src/schema/type';

const isDevelopment = process.env.NODE_ENV === 'development';
const isProduction = process.env.NODE_ENV === 'production';

const EUREKA_DISABLED = process.env.EUREKA_DISABLED === 'true' || false;
const SERVER_IP = process.env.SERVER_IP;
const SERVER_PORT = Number(process.env.SERVER_PORT);
const SERVER_URL = process.env.SERVER_URL;
const ROUND = Number(process.env.HASH_ROUND);
const SECRET_KEY = process.env.SECRET_KEY as Secret;
const DATABASE_URL = process.env.DATABASE_URL;
const ACCESS_TOKEN_EXPIRES = process.env.ACCESS_TOKEN_EXPIRES;
const REFRESH_TOKEN_EXPIRES = process.env.REFRESH_TOKEN_EXPIRES;
const REDIS_HOST = process.env.REDIS_HOST;
const REDIS_PORT = process.env.REDIS_PORT;
const REDIS_BINDING_PORT = process.env.REDIS_BINDING_PORT;
const REDIS_CONTAINER_NAME = process.env.REDIS_CONTAINER_NAME;
const REDIS_IMAGE = process.env.REDIS_IMAGE;
const REDIS_VOLUME = process.env.REDIS_VOLUME;

const AUTH_PREFIX = 'AUTH';

const ERROR_MESSAGE: Record<string, CommonResponseType> = {
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
  tooManyRequests: {
    code: `${AUTH_PREFIX}013`,
    message: 'Too Many Requests',
  },
  authHeaderRequired: {
    code: `${AUTH_PREFIX}014`,
    message: 'Authorization header is required',
  },
  verifyAccessTokenFailed: {
    code: `${AUTH_PREFIX}015`,
    message: 'Verify Access Token Failed',
  },
  verifyRefreshTokenFailed: {
    code: `${AUTH_PREFIX}016`,
    message: 'Verify Refresh Token Failed',
  },
  accessTokenDecodeFailed: {
    code: `${AUTH_PREFIX}017`,
    message: 'Access Token Decode Failed',
  },
  duplicateNickname: {
    code: `${AUTH_PREFIX}018`,
    message: 'Duplicate Nickname',
  },
  loginStatusDisabled: {
    code: `${AUTH_PREFIX}019`,
    message: 'Login Status Disabled',
    result: {
      status: false,
    },
  },
} as const;

const SUCCESS_MESSAGE: Record<string, CommonResponseType> = {
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
  healthCheckOk: {
    code: `${AUTH_PREFIX}108`,
    message: 'health check success!',
  },
  loginStatusOK: {
    code: `${AUTH_PREFIX}109`,
    message: 'login status is enabled',
    result: {
      status: true,
    },
  },
} as const;

const REDIS_KEY = {
  rateLimit: (identifier: string) => `rateLimit:${identifier}`,
  blocked: (identifier: string) => `blocked:${identifier}`,
  refreshToken: (userId: number) => `refreshToken:${userId}`,
} as const;

export {
  isDevelopment,
  isProduction,
  SERVER_IP,
  SERVER_PORT,
  SERVER_URL,
  DATABASE_URL,
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
  EUREKA_DISABLED,
};
