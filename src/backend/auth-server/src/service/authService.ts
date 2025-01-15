import {
  generateAccessToken,
  generateRefreshToken,
  verifyPassword,
  verifyRefreshToken,
} from '@/libs/authHelper';
import { ERROR_MESSAGE } from '@/libs/constants';
import db from '@/libs/db';
import { getCurrentDate } from '@/libs/timeHelper';

function authService() {
  const register = async (
    email: string,
    password: string,
    name: string,
    nickname: string,
    birthdate: Date,
  ) => {
    const result = await db.uSER.create({
      data: {
        email,
        password,
        name,
        nickname,
        create_at: getCurrentDate(),
        update_at: getCurrentDate(),
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

      const accessToken = generateAccessToken(authenticationUser);
      const refreshToken = generateRefreshToken(authenticationUser);

      const values = {
        userId: authenticationUser.id,
        accessToken,
        refreshToken,
      };

      //@TODO: 토큰 저장

      const returnValue = {
        id: authenticationUser.id,
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

  const logout = async () => {
    try {
      //@TODO: 토큰 삭제
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  const refresh = async (refreshToken: string) => {
    try {
      if (!refreshToken) throw ERROR_MESSAGE.unauthorized;

      const authenticationUser = await verifyRefreshToken(refreshToken);

      // Todo: 토큰 검증 후 유저 정보 반환, id, email, Authorization
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  return {
    register,
    loginWithPassword,
    logout,
    refresh,
  };
}

export default authService();
