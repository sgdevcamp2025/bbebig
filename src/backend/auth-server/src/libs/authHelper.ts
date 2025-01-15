import jwt, { JwtPayload } from 'jsonwebtoken';
import {
  ACCESS_TOKEN_EXPIRES,
  REFRESH_TOKEN_EXPIRES,
  SECRET_KEY,
  ROUND,
  ERROR_MESSAGE,
} from './constants';
import bcrypt from 'bcrypt';
import db from './db';

const generateHash = (pwd: string) => {
  return bcrypt.hashSync(pwd, ROUND);
};

const duplicateVerifyUser = async (email: string) => {
  try {
    const userCount = await db.uSER.count({
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
    const encryptedPwd = await db.uSER.findUnique({
      where: {
        email,
      },
      select: {
        password: true,
      },
    });

    if (!encryptedPwd?.password) return false;

    const result = bcrypt.compareSync(password, encryptedPwd.password);
    return result;
  } catch (error) {
    throw error;
  }
};

const generateAccessToken = (user: { id: bigint; email: string }) => {
  const accessToken = jwt.sign({ id: user.id, email: user.email }, SECRET_KEY, {
    expiresIn: ACCESS_TOKEN_EXPIRES,
  });
  return accessToken;
};

const generateRefreshToken = (user: { id: bigint; email: string }) => {
  const refreshToken = jwt.sign({ id: user.id, email: user.email }, SECRET_KEY, {
    expiresIn: REFRESH_TOKEN_EXPIRES,
  });
  return refreshToken;
};

const verifyRefreshToken = async (refreshToken: string) => {
  try {
    const decoded = jwt.verify(refreshToken, SECRET_KEY) as JwtPayload;

    // Todo: 토큰 검증 후 유저 조회
    return decoded;
  } catch (error) {
    throw error;
  }
};

export {
  generateHash,
  generateAccessToken,
  generateRefreshToken,
  duplicateVerifyUser,
  verifyPassword,
  verifyRefreshToken,
};
