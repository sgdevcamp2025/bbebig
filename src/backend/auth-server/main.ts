import 'dotenv/config';
import Fastify from 'fastify';
import {
  createJsonSchemaTransformObject,
  isResponseSerializationError,
  jsonSchemaTransform,
  serializerCompiler,
  validatorCompiler,
  ZodTypeProvider,
} from 'fastify-type-provider-zod';
import { FastifyCookieOptions } from '@fastify/cookie';
import routes from './src/routes';
import cors from '@fastify/cors';
import fastifyCookie from '@fastify/cookie';
import { PORT, REDIS_HOST, REDIS_PORT, SECRET_KEY } from '@/libs/constants';
import fastifyRedis from '@fastify/redis';
import { currentAuthPlugin } from '@/plugin/authPlugin';
import { fastifySwagger } from '@fastify/swagger';
import fastifySwaggerUi from '@fastify/swagger-ui';
import { z } from 'zod';
const app = Fastify({
  logger: true,
}).withTypeProvider<ZodTypeProvider>();

app.setValidatorCompiler(validatorCompiler);
app.setSerializerCompiler(serializerCompiler);

const USER_SCHEMA = z.object({
  id: z.number(),
  email: z.string(),
});

app.register(fastifySwagger, {
  openapi: {
    info: {
      title: 'Bbebig Auth Service API',
      description: 'Bbebig auth service',
      version: '1.0.0',
    },
    servers: [],
  },
  transform: jsonSchemaTransform,
  transformObject: createJsonSchemaTransformObject({
    schemas: {
      User: USER_SCHEMA,
    },
  }),
});

app.register(fastifySwaggerUi, {
  routePrefix: '/docs',
  uiConfig: {
    docExpansion: 'full',
    deepLinking: true,
  },
});

app.register(currentAuthPlugin);
app.register(routes);
app.register(fastifyRedis, {
  host: REDIS_HOST,
  port: Number(REDIS_PORT),
});

app.register(cors, {
  origin: true,
  credentials: true,
});
app.register(fastifyCookie, {
  secret: SECRET_KEY,
} as FastifyCookieOptions);

import { hasZodFastifySchemaValidationErrors } from 'fastify-type-provider-zod';

app.setErrorHandler((err, req, reply) => {
  if (hasZodFastifySchemaValidationErrors(err)) {
    return reply.code(400).send({
      message: 'Response Validation Error',
      code: 400,
      isSuccess: false,
      details: {
        issues: err.validation,
        method: req.method,
        url: req.url,
      },
    });
  }

  if (isResponseSerializationError(err)) {
    return reply.code(500).send({
      message: 'Internal Server Error',
      code: 500,
      isSuccess: false,
      details: {
        issues: err.cause.issues,
        method: err.method,
        url: err.url,
      },
    });
  }
});

const start = async () => {
  try {
    await app.listen({ port: Number(PORT) });
    console.log(`listening on port ${PORT}`);
  } catch (error) {
    app.log.error(error);
    process.exit(1);
  }
};

start();
