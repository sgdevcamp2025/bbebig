import { FastifyRequest, FastifyReply } from 'fastify';
import { generateHash } from '../../libs/authHelper';
import { ERROR_MESSAGE, REDIS_KEY } from '../../libs/constants';
import { SUCCESS_MESSAGE } from '../../libs/constants';
import { handleError } from '../../libs/errorHelper';
import authService from '../../service/authService';
import handleSuccess from '../../libs/responseHelper';
import redis from '../../libs/redis';
function authController() {
  const login = async (req: FastifyRequest, res: FastifyReply) => {
    try {
      const { email, password } = req.body as { email: string; password: string };

      if (!email || !password) {
        handleError(res, ERROR_MESSAGE.badRequest);
        return;
      }

      const values = await authService.loginWithPassword(email, password);

      if (!values) {
        handleError(res, ERROR_MESSAGE.notFound);
        return;
      }

      res.setCookie('refresh_token', values.refreshToken, {
        sameSite: true,
        httpOnly: true,
        secure: true,
        path: '/',
        expires: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
      });

      const result = {
        accessToken: values.accessToken,
      };

      await redis.set(REDIS_KEY.refreshToken(values.id), values.refreshToken);

      handleSuccess(
        res,
        {
          ...SUCCESS_MESSAGE.loginOk,
          result,
        },
        200,
      );
    } catch (error) {
      console.error(error);
      if (error.code === ERROR_MESSAGE.notFound.code) {
        handleError(res, ERROR_MESSAGE.notFound, error);
      }

      if (error.code === ERROR_MESSAGE.passwordNotMatch.code) {
        handleError(res, ERROR_MESSAGE.passwordNotMatch, error);
      }

      if (error.code === ERROR_MESSAGE.badRequest.code) {
        handleError(res, ERROR_MESSAGE.badRequest, error);
      }

      if (error.code === ERROR_MESSAGE.tooManyRequests.code) {
        handleError(res, ERROR_MESSAGE.tooManyRequests, error);
      }

      handleError(res, ERROR_MESSAGE.serverError, error);
    }
  };

  const register = async (req: FastifyRequest, res: FastifyReply) => {
    const { email, password, name, nickname, birthDate } = req.body as {
      email: string;
      password: string;
      name: string;
      nickname: string;
      birthDate: string;
    };

    try {
      const hashedPassword = generateHash(password);

      await authService.register(email, hashedPassword, name, nickname, new Date(birthDate));

      handleSuccess(res, SUCCESS_MESSAGE.registerOk, 201);
    } catch (error) {
      handleError(res, ERROR_MESSAGE.duplicateEmail, error);
    }
  };

  const logout = async (req: FastifyRequest, res: FastifyReply) => {
    const id = req.user?.id;
    const refreshToken = req.cookies.refresh_token;

    if (!id || !refreshToken) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    try {
      await redis.del(REDIS_KEY.refreshToken(id));

      res.clearCookie('refresh_token', {
        path: '/',
      });

      handleSuccess(res, SUCCESS_MESSAGE.logoutOk, 205);
    } catch (error) {
      handleError(res, ERROR_MESSAGE.badRequest, error);
    }
  };

  const refresh = async (req: FastifyRequest, res: FastifyReply) => {
    const id = req.user?.id;
    const refreshToken = req.cookies.refresh_token;

    if (!refreshToken || !id) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    try {
      const redisRefreshToken = await redis.get(REDIS_KEY.refreshToken(id));

      if (!redisRefreshToken) {
        handleError(res, ERROR_MESSAGE.unauthorized);
        return;
      }

      const result = await authService.refresh(refreshToken, redisRefreshToken);

      await redis.set(REDIS_KEY.refreshToken(id), result.refreshToken);

      handleSuccess(res, {
        ...SUCCESS_MESSAGE.refreshToken,
        result,
      });
    } catch (error) {
      handleError(res, ERROR_MESSAGE.unauthorized, error);
    }
  };

  const verifyToken = async (req: FastifyRequest, res: FastifyReply) => {
    const accessToken = req.headers.authorization;
    if (!accessToken) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    try {
      const result = await authService.verifyToken(accessToken);

      handleSuccess(
        res,
        {
          ...SUCCESS_MESSAGE.verifyTokenOk,
          result,
        },
        200,
      );
    } catch (error) {
      handleError(res, ERROR_MESSAGE.verifyTokenFailed, error);
    }
  };

  const healthCheck = async (req: FastifyRequest, res: FastifyReply) => {
    handleSuccess(res, SUCCESS_MESSAGE.healthCheckOk, 200);
  };

  return {
    login,
    register,
    logout,
    refresh,
    verifyToken,
    healthCheck,
  };
}

export default authController();
