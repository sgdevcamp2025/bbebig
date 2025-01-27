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
import {
  ERROR_MESSAGE,
  REDIS_HOST,
  REDIS_PORT,
  SECRET_KEY,
  SERVER_PORT,
} from './src/libs/constants';
import fastifyRedis from '@fastify/redis';
import { currentAuthPlugin } from './src/plugin/authPlugin';
import { fastifySwagger } from '@fastify/swagger';
import fastifySwaggerUi from '@fastify/swagger-ui';
import { z } from 'zod';
import { hasZodFastifySchemaValidationErrors } from 'fastify-type-provider-zod';
import { handleError } from 'src/libs/errorHelper';

const app = Fastify({
  logger: true,
  pluginTimeout: 20000,
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

app
  .register(fastifyRedis, {
    host: REDIS_HOST,
    port: Number(REDIS_PORT),
    closeClient: false,
    connectTimeout: 10000,
  })
  .ready((err) => {
    if (err) {
      console.error('Redis connection failed:', err);
    } else {
      console.log('Redis connected successfully');
    }
  });

app.register(cors, {
  origin: true,
  credentials: true,
});
app.register(fastifyCookie, {
  secret: SECRET_KEY,
} as FastifyCookieOptions);

app.setErrorHandler((err, req, reply) => {
  if (hasZodFastifySchemaValidationErrors(err)) {
    return handleError(
      reply,
      {
        ...ERROR_MESSAGE.badRequest,
        message: err.validation[0].params.issue.message,
      },
      err,
    );
  }

  if (isResponseSerializationError(err)) {
    return handleError(reply, ERROR_MESSAGE.serverError, err);
  }
});

const start = async () => {
  try {
    await app.ready(() => {
      console.log('Registered routes:', app.printRoutes());
    });

    await app.listen({ port: Number(SERVER_PORT), host: '0.0.0.0' });
    console.log(`listening on port ${SERVER_PORT}`);
  } catch (error) {
    app.log.error(error);
    process.exit(1);
  }
};

start();
