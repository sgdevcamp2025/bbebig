import {
  generateAccessToken,
  generateRefreshToken,
  verifyAccessToken,
  verifyPassword,
  verifyRefreshToken,
} from '@/libs/authHelper';
import { ERROR_MESSAGE } from '@/libs/constants';
import db from '@/libs/db';

function authService() {
  const register = async (
    email: string,
    password: string,
    name: string,
    nickname: string,
    birthdate: string,
  ) => {
    const result = await db.uSER.create({
      data: {
        email,
        password,
        name,
        nickname,
        birthdate,
      },
    });

    return result;
  };

  const loginWithPassword = async (email: string, password: string) => {
    try {
      const authenticationUser = await db.uSER.findFirst({
        where: {
          email,
        },
        select: {
          id: true,
          email: true,
        },
      });

      if (!authenticationUser) throw new Error('User not found');

      const isPasswordCorrect = await verifyPassword(email, password);
      if (!isPasswordCorrect) throw ERROR_MESSAGE.unauthorized;

      const accessToken = generateAccessToken({
        id: Number(authenticationUser.id),
        email: authenticationUser.email,
      });
      const refreshToken = generateRefreshToken({
        id: Number(authenticationUser.id),
        email: authenticationUser.email,
      });

      const returnValue = {
        id: Number(authenticationUser.id),
        email: authenticationUser.email,
        accessToken,
        refreshToken,
      };

      return returnValue;
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  const refresh = async (refreshToken: string, redisRefreshToken: string) => {
    try {
      if (!refreshToken || !redisRefreshToken) throw ERROR_MESSAGE.unauthorized;

      const authenticationUser = await verifyRefreshToken(refreshToken, redisRefreshToken);

      const userInfo = {
        id: authenticationUser.id,
        email: authenticationUser.email,
      };

      const accessToken = generateAccessToken(userInfo);

      return {
        accessToken,
      };
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  return {
    register,
    loginWithPassword,
    refresh,
  };
}

export default authService();
