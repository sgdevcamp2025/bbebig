import { FastifyPluginAsync, FastifyRequest } from 'fastify';
import { verifyAccessToken, shortVerifyRefreshToken } from '@/libs/authHelper';
import { ERROR_MESSAGE } from '@/libs/constants';
import fp from 'fastify-plugin';

const currentAuth: FastifyPluginAsync = async (app) => {
  app.decorateRequest('user', null);
  app.addHook('preHandler', async (req: FastifyRequest) => {
    const { authorization } = req.headers;
    const refresh_token = req.cookies.refresh_token;

    if (!authorization || !refresh_token) return;

    try {
      shortVerifyRefreshToken(refresh_token);
      const decode = await verifyAccessToken(authorization);

      req.user = {
        id: decode.id,
        email: decode.email,
      };
    } catch (error) {
      throw ERROR_MESSAGE.unauthorized;
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
