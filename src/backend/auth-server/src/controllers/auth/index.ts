import { FastifyInstance, FastifyReply } from 'fastify';
import { FastifyRequest } from 'fastify';
import { generateHash, verifyAccessToken } from 'src/libs/authHelper';
import { ERROR_MESSAGE, REDIS_KEY } from 'src/libs/constants';
import { SUCCESS_MESSAGE } from 'src/libs/constants';
import { handleError } from 'src/libs/errorHelper';
import authService from 'src/service/authService';

function authController() {
  const login = async (req: FastifyRequest, res: FastifyReply, app: FastifyInstance) => {
    const { email, password } = req.body as { email: string; password: string };

    const values = await authService.loginWithPassword(email, password);

    res.setCookie('refresh_token', values.refreshToken, {
      sameSite: true,
      httpOnly: true,
      secure: true,
      path: '/',
      expires: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
    });

    const result = {
      email: values.email,
      accessToken: values.accessToken,
    };

    app.redis.set(REDIS_KEY.refreshToken(values.id), values.refreshToken);

    return {
      ...SUCCESS_MESSAGE.loginOk,
      details: result,
    };
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

      return SUCCESS_MESSAGE.registerOk;
    } catch (error) {
      handleError(res, ERROR_MESSAGE.duplicateEmail, error);
    }
  };

  const logout = async (req: FastifyRequest, res: FastifyReply, app: FastifyInstance) => {
    const id = req.user?.id;
    const refreshToken = req.cookies.refresh_token;

    if (!id || !refreshToken) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    try {
      await app.redis.del(REDIS_KEY.refreshToken(id));

      res.clearCookie('refresh_token', {
        path: '/',
      });

      return SUCCESS_MESSAGE.logoutOk;
    } catch (error) {
      handleError(res, ERROR_MESSAGE.badRequest, error);
    }
  };

  const refresh = async (req: FastifyRequest, res: FastifyReply, app: FastifyInstance) => {
    const id = req.user?.id;
    const refreshToken = req.cookies.refresh_token;

    if (!refreshToken || !id) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    try {
      const redisRefreshToken = await app.redis.get(REDIS_KEY.refreshToken(id));

      if (!redisRefreshToken) {
        handleError(res, ERROR_MESSAGE.unauthorized);
        return;
      }

      const values = await authService.refresh(refreshToken, redisRefreshToken);

      return {
        ...SUCCESS_MESSAGE.refreshToken,
        details: values,
      };
    } catch (error) {
      handleError(res, ERROR_MESSAGE.unauthorized, error);
    }
  };

  const verifyToken = async (req: FastifyRequest, res: FastifyReply) => {
    const authorization = req.headers.authorization;
    if (!authorization) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    const decode = await verifyAccessToken(authorization);

    if (!decode) {
      handleError(res, ERROR_MESSAGE.unauthorized);
      return;
    }

    return {
      ...SUCCESS_MESSAGE.accessTokenOk,
      details: {
        id: decode.id,
        email: decode.email,
      },
    };
  };

  return {
    login,
    register,
    logout,
    refresh,
    verifyToken,
  };
}

export default authController();
