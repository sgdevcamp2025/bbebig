import Fastify from 'fastify';
import mockRoutes from './route';
import { validatorCompiler, serializerCompiler } from 'fastify-type-provider-zod';

const fastify = Fastify({
  logger: true,
});

fastify.setValidatorCompiler(validatorCompiler);
fastify.setSerializerCompiler(serializerCompiler);

fastify.register(mockRoutes);

const startServers = async () => {
  try {
    await fastify.listen({ port: 9090 });
  } catch (error) {
    process.exit(1);
  }
};

startServers();
