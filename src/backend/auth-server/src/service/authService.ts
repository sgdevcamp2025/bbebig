import {
  accessTokenDecode,
  generateAccessToken,
  generateRefreshToken,
  shortAccessTokenDecode,
  verifyPassword,
  verifyRefreshToken,
} from '../libs/authHelper';
import { ERROR_MESSAGE } from '../libs/constants';
import db from '../libs/db';

function authService() {
  const register = async (
    email: string,
    password: string,
    name: string,
    nickname: string,
    birth_date: Date,
  ) => {
    const result = await db.member.create({
      data: {
        email,
        password,
        name,
        nickname,
        birth_date,
      },
    });

    return result;
  };

  const loginWithPassword = async (email: string, password: string) => {
    try {
      const authenticationUser = await db.member.findFirst({
        where: {
          email,
        },
        select: {
          id: true,
          email: true,
          nickname: true,
        },
      });

      if (!authenticationUser) return false;

      const isPasswordCorrect = await verifyPassword(email, password);

      if (!isPasswordCorrect) return false;

      const accessToken = generateAccessToken({
        id: Number(authenticationUser.id),
      });
      const refreshToken = generateRefreshToken({
        id: Number(authenticationUser.id),
      });

      const returnValue = {
        id: Number(authenticationUser.id),
        email: authenticationUser.email,
        nickname: authenticationUser.nickname,
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
      };

      const newAccessToken = generateAccessToken(userInfo);
      const newRefreshToken = generateRefreshToken(userInfo);

      return {
        accessToken: newAccessToken,
        refreshToken: newRefreshToken,
      };
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  const verifyToken = async (accessToken: string) => {
    try {
      const isTokenValid = await shortAccessTokenDecode(accessToken);
      if (!isTokenValid) throw ERROR_MESSAGE.verifyTokenFailed;

      const authenticationUser = await accessTokenDecode(accessToken);

      const userInfo = {
        memberId: authenticationUser.id,
        valid: true,
      };

      return userInfo;
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  return {
    register,
    loginWithPassword,
    refresh,
    verifyToken,
  };
}

export default authService();
