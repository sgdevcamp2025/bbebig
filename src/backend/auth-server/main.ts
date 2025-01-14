import Fastify, { FastifyRequest, FastifyReply } from 'fastify';

const fastify = Fastify({
  logger: true,
});

fastify.get('/', (req: FastifyRequest, res: FastifyReply) => {
  res.send('Hello World');
});

fastify.listen({ port: 8083 });
