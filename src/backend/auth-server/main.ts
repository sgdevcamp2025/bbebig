import 'dotenv/config';
import Fastify from 'fastify';
import { serializerCompiler, validatorCompiler, ZodTypeProvider } from 'fastify-type-provider-zod';
import { FastifyCookieOptions } from '@fastify/cookie';
import routes from './src/routes';
import cors from '@fastify/cors';
import fastifyCookie from '@fastify/cookie';
import { REDIS_HOST, REDIS_PASSWORD, REDIS_PORT, SECRET_KEY } from '@/libs/constants';
import fastifyRedis from '@fastify/redis';
import { currentAuthPlugin } from '@/plugin/authPlugin';
const app = Fastify({
  logger: true,
}).withTypeProvider<ZodTypeProvider>();

const port = process.env.PORT || 8083;

app.register(currentAuthPlugin);
app.register(routes);
app.register(fastifyRedis, {
  host: REDIS_HOST,
  port: Number(REDIS_PORT),
  password: REDIS_PASSWORD,
});

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
