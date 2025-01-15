import { FastifyInstance } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import { accessTokenSchema, refreshTokenSchema } from '../../schema/authSchema';

const authRoute = async (app: FastifyInstance) => {
  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/login',
    handler: (req, res) => {
      res.send({ message: 'Hello World' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/signup',
    handler: (req, res) => {
      res.send({ message: 'Hello World' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/logout',
    handler: (req, res) => {
      res.send({ message: 'Hello World' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/access',
    schema: accessTokenSchema,
    handler: (req, res) => {
      res.send({ accessToken: '123', refreshToken: '123' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/refresh',
    schema: refreshTokenSchema,
    handler: (req, res) => {
      res.send({ accessToken: '123', refreshToken: '123' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'GET',
    url: '/check',
    handler: (req, res) => {
      res.send({ isSuccess: true });
    },
  });
};

export default authRoute;
