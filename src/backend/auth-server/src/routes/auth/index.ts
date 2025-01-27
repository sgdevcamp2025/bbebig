import { FastifyInstance, FastifyReply, FastifyRequest } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import {
  logoutSchema,
  refreshTokenSchema,
  registerSchema,
  signInSchema,
  tokenDecodeSchema,
  verifyEmailSchema,
  verifyTokenSchema,
} from '../../schema/authSchema';
import { verifySignIn } from '../../libs/authHelper';
import { authController } from 'src/controllers';

const authRoute = async (app: FastifyInstance) => {
  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/login',
    schema: signInSchema,
    handler: authController.login,
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/register',
    schema: registerSchema,
    handler: authController.register,
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/logout',
    schema: logoutSchema,
    preHandler: [verifySignIn],
    handler: authController.logout,
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'PUT',
    url: '/refresh',
    schema: refreshTokenSchema,
    preHandler: [verifySignIn],
    handler: authController.refresh,
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/verify-token',
    schema: tokenDecodeSchema,
    handler: authController.verifyToken,
  });
};

export default authRoute;
