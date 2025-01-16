import { FastifyInstance } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import {
  logoutSchema,
  refreshTokenSchema,
  registerSchema,
  signInSchema,
  verifyTokenSchema,
} from '../../schema/authSchema';

import authService from '@/service/authService';
import { DOMAIN, ERROR_MESSAGE, REDIS_KEY, SUCCESS_MESSAGE } from '@/libs/constants';
import { handleError } from '@/libs/errorHelper';
import { generateHash, verifyAccessToken, verifySignIn } from '@/libs/authHelper';

const authRoute = async (app: FastifyInstance) => {
  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/login',
    schema: signInSchema,
    handler: async (req, res) => {
      const { email, password } = req.body as { email: string; password: string };
      const values = await authService.loginWithPassword(email, password);

      res.setCookie('refresh_token', values.refreshToken, {
        domain: DOMAIN,
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
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/register',
    schema: registerSchema,
    handler: async (req, res) => {
      const { email, password, name, nickname, birthdate } = req.body;

      try {
        const hashedPassword = generateHash(password);

        await authService.register(email, hashedPassword, name, nickname, birthdate);

        return SUCCESS_MESSAGE.registerOk;
      } catch (error) {
        if (error.code === 'P2002') handleError(res, ERROR_MESSAGE.duplicateEmail, error);
      }
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/logout',
    schema: logoutSchema,
    preHandler: [verifySignIn],
    handler: async (req, res) => {
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
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'PUT',
    url: '/refresh',
    schema: refreshTokenSchema,
    preHandler: [verifySignIn],
    handler: async (req, res) => {
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
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/verify-token',
    schema: verifyTokenSchema,
    handler: async (req, res) => {
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
    },
  });
};

export default authRoute;
