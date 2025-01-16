import 'dotenv/config';
import Fastify from 'fastify';
import { serializerCompiler, validatorCompiler, ZodTypeProvider } from 'fastify-type-provider-zod';
import { FastifyCookieOptions } from '@fastify/cookie';
import routes from './src/routes';
import cors from '@fastify/cors';
import fastifyCookie from '@fastify/cookie';
import { SECRET_KEY } from '@/libs/constants';

const app = Fastify({
  logger: true,
}).withTypeProvider<ZodTypeProvider>();

const port = process.env.PORT || 8083;

app.register(routes);

// TODO: 프론트 오리진 설정
app.register(cors, {
  origin: true,
  credentials: true,
});
app.register(fastifyCookie, {
  secret: SECRET_KEY,
} as FastifyCookieOptions);
app.setValidatorCompiler(validatorCompiler);
app.setSerializerCompiler(serializerCompiler);

const start = async () => {
  try {
    await app.listen({ port: Number(port) });
    console.log(`listening on port ${port}`);
  } catch (error) {
    app.log.error(error);
    process.exit(1);
  }
};

start();
