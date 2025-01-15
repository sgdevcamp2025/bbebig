import { FastifyInstance } from 'fastify';
import userRoute from './user';

const mockRoutes = async (fastify: FastifyInstance) => {
  await fastify.register(userRoute, { prefix: '/user' });
};

export default mockRoutes;
