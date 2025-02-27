import jwt, { JwtPayload } from 'jsonwebtoken';
import {
  ACCESS_TOKEN_EXPIRES,
  REFRESH_TOKEN_EXPIRES,
  SECRET_KEY,
  ROUND,
  ERROR_MESSAGE,
  REDIS_KEY,
} from './constants';
import bcrypt from 'bcrypt';
import db from './db';
import { FastifyReply, FastifyRequest } from 'fastify';
import { handleError } from './errorHelper';
import { checkRateLimit } from './rateLimit';
import redis from './redis';

const generateHash = (pwd: string) => {
  return bcrypt.hashSync(pwd, ROUND);
};

const duplicateVerifyUser = async (email: string) => {
  try {
    const userCount = await db.member.count({
      where: { email },
    });

    if (userCount > 0) throw ERROR_MESSAGE.alreadySignup;

    return true;
  } catch (error) {
    throw error;
  }
};

const verifyPassword = async (email: string, password: string) => {
  try {
    const identifier = email;

    await checkRateLimit(identifier);

    const encryptedPwd = await db.member.findUnique({
      where: {
        email,
      },
      select: {
        password: true,
      },
    });

    if (!encryptedPwd?.password) throw ERROR_MESSAGE.notFound;

    const isPasswordCorrect = bcrypt.compareSync(password, encryptedPwd.password);
    if (!isPasswordCorrect) throw ERROR_MESSAGE.passwordNotMatch;

    await redis.del(REDIS_KEY.rateLimit(identifier));
    await redis.del(REDIS_KEY.blocked(identifier));

    return true;
  } catch (error) {
    throw error;
  }
};

const generateAccessToken = (user: { id: number }) => {
  const accessToken = jwt.sign({ id: user.id }, SECRET_KEY, {
    expiresIn: ACCESS_TOKEN_EXPIRES,
  });
  return accessToken;
};

const generateRefreshToken = (user: { id: number }) => {
  const refreshToken = jwt.sign({ id: user.id }, SECRET_KEY, {
    expiresIn: REFRESH_TOKEN_EXPIRES,
  });
  return refreshToken;
};

const verifySignIn = async (req: FastifyRequest, res: FastifyReply) => {
  const id = req.user?.id;

  if (id) {
    return;
  } else {
    handleError(res, ERROR_MESSAGE.unauthorized);
  }
};

const verifyRefreshToken = async (refreshToken: string, redisRefreshToken?: string) => {
  if (refreshToken === redisRefreshToken) {
    const decoded = jwt.verify(refreshToken, SECRET_KEY) as JwtPayload;
    return decoded;
  } else {
    throw ERROR_MESSAGE.verifyRefreshTokenFailed;
  }
};

const shortVerifyRefreshToken = async (refreshToken: string) => {
  try {
    const decoded = jwt.verify(refreshToken, SECRET_KEY) as JwtPayload;
    return Boolean(decoded);
  } catch (error) {
    throw ERROR_MESSAGE.verifyRefreshTokenFailed;
  }
};

const verifyAccessToken = async (accessToken: string) => {
  try {
    const token = accessToken.split(' ')[1];
    const decoded = jwt.verify(token, SECRET_KEY) as JwtPayload;
    return decoded;
  } catch (error) {
    throw ERROR_MESSAGE.verifyAccessTokenFailed;
  }
};

const shortAccessTokenDecode = async (accessToken: string) => {
  try {
    const token = accessToken.split(' ')[1];
    const decoded = jwt.verify(token, SECRET_KEY) as JwtPayload;
    return Boolean(decoded);
  } catch (error) {
    throw ERROR_MESSAGE.verifyAccessTokenFailed;
  }
};

const accessTokenDecode = async (accessToken: string) => {
  try {
    const token = accessToken.split(' ')[1];
    const decoded = jwt.verify(token, SECRET_KEY) as JwtPayload;
    return decoded;
  } catch (error) {
    throw ERROR_MESSAGE.accessTokenDecodeFailed;
  }
};

export {
  generateHash,
  generateAccessToken,
  generateRefreshToken,
  duplicateVerifyUser,
  verifyPassword,
  verifyRefreshToken,
  verifyAccessToken,
  shortVerifyRefreshToken,
  verifySignIn,
  accessTokenDecode,
  shortAccessTokenDecode,
};
