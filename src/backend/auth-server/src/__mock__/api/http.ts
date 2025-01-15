import Fastify from 'fastify';
import mockRoutes from './route';

const fastify = Fastify({
  logger: true,
});

fastify.register(mockRoutes);

const startServers = async () => {
  try {
    await fastify.listen({ port: 9090 });
  } catch (error) {
    process.exit(1);
  }
};

startServers();
