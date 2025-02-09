import { FastifyPluginAsync, FastifyReply } from 'fastify';
import fp from 'fastify-plugin';
import { FastifyRequest } from 'fastify';
import { verifyAccessToken, shortVerifyRefreshToken } from '../libs/authHelper';
import { ERROR_MESSAGE } from '../libs/constants';

const currentAuth: FastifyPluginAsync = async (app) => {
  app.decorateRequest('user', null);
  app.addHook('preHandler', async (req: FastifyRequest, res: FastifyReply) => {
    const requiresAuth = req.routeOptions.schema?.security?.some(
      (security) => security.bearerAuth !== undefined,
    );

    if (!requiresAuth) return;

    const { authorization } = req.headers;
    const refresh_token = req.cookies.refresh_token;

    if (!authorization) {
      req.log.error({
        message: 'Token is required',
      });
      throw ERROR_MESSAGE.unauthorized;
    }

    try {
      const decode = await verifyAccessToken(authorization);

      if (refresh_token) {
        shortVerifyRefreshToken(refresh_token);
      }

      req.user = {
        id: decode.id,
        email: decode.email,
      };
    } catch (error) {
      if (error.name === 'TokenExpiredError') {
        req.log.error({
          message: 'Token has expired',
          error,
        });
        throw error;
      } else {
        req.log.error({
          message: 'Token verification failed',
          error,
        });
        throw error;
      }
    }
  });
};

export const currentAuthPlugin = fp(currentAuth, {
  name: 'currentAuthPlugin',
});

declare module 'fastify' {
  interface FastifyRequest {
    user: {
      id: number;
      email: string;
    } | null;
  }
}
