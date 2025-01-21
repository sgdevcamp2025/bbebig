import { FastifyInstance } from 'fastify';
import authRoute from './auth';

const routes = (fastify: FastifyInstance) => {
  fastify.register(authRoute, { prefix: '/auth-server' });
};

export default routes;
