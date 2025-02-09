import { FastifyInstance } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import {
  healthCheckSchema,
  logoutSchema,
  refreshTokenSchema,
  registerSchema,
  signInSchema,
  verifyTokenSchema,
} from '../../schema/authSchema';
import { verifySignIn } from '../../libs/authHelper';
import { authController } from '../../controllers';

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
    schema: verifyTokenSchema,
    preHandler: [verifySignIn],
    handler: authController.verifyToken,
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/health-check',
    schema: healthCheckSchema,
    handler: authController.healthCheck,
  });
};

export default authRoute;
