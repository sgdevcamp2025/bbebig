import { FastifyInstance, FastifyReply } from 'fastify';
import { FastifyRequest } from 'fastify';
import { generateHash } from 'src/libs/authHelper';
import { ERROR_MESSAGE, REDIS_KEY } from 'src/libs/constants';
import { SUCCESS_MESSAGE } from 'src/libs/constants';
import { handleError } from 'src/libs/errorHelper';
import authService from 'src/service/authService';
import handleSuccess from 'src/libs/responseHelper';
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
      accessToken: values.accessToken,
    };

    app.redis.set(REDIS_KEY.refreshToken(values.id), values.refreshToken);

    handleSuccess(res, {
      ...SUCCESS_MESSAGE.loginOk,
      result,
    });
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

      handleSuccess(res, SUCCESS_MESSAGE.registerOk);
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

      handleSuccess(res, SUCCESS_MESSAGE.logoutOk);
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

      const result = await authService.refresh(refreshToken, redisRefreshToken);

      app.redis.set(REDIS_KEY.refreshToken(id), result.refreshToken);

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

      handleSuccess(res, {
        ...SUCCESS_MESSAGE.verifyTokenOk,
        result,
      });
    } catch (error) {
      handleError(res, ERROR_MESSAGE.verifyTokenFailed, error);
    }
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
