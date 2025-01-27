import { FastifyInstance, FastifyReply, FastifyRequest } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import {
  logoutSchema,
  refreshTokenSchema,
  registerSchema,
  signInSchema,
  verifyEmailSchema,
  verifyTokenSchema,
} from '../../schema/authSchema';
import { verifySignIn } from '../../libs/authHelper';
import { authController } from 'src/controllers';

const authRoute = async (app: FastifyInstance) => {
  const instanceWrapper = (
    controller: (req: FastifyRequest, res: FastifyReply, app: FastifyInstance) => any,
  ) => {
    return async (req: FastifyRequest, res: FastifyReply) => {
      await controller(req, res, app);
    };
  };

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/login',
    schema: signInSchema,
    handler: instanceWrapper(authController.login),
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
    handler: instanceWrapper(authController.logout),
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'PUT',
    url: '/refresh',
    schema: refreshTokenSchema,
    preHandler: [verifySignIn],
    handler: instanceWrapper(authController.refresh),
  });
};

export default authRoute;
