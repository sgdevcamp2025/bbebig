import { FastifyInstance } from 'fastify';

const userRoute = async (fastify: FastifyInstance) => {
  fastify.get('/', (req, res) => {
    res.send({ message: 'Hello World' });
  });
  fastify.post('/', (req, res) => {
    res.send({ message: 'Hello World' });
  });
};

export default userRoute;
