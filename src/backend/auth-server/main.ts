import Fastify, { FastifyRequest, FastifyReply } from 'fastify';
import { serializerCompiler, validatorCompiler, ZodTypeProvider } from 'fastify-type-provider-zod';
import cookie from '@fastify/cookie';

const app = Fastify({
  logger: true,
});

app.setValidatorCompiler(validatorCompiler);
app.setSerializerCompiler(serializerCompiler);

app.get('/', (req: FastifyRequest, res: FastifyReply) => {
  res.send('Hello World');
});

app.register(cookie);

app.listen({ port: 8083 });
