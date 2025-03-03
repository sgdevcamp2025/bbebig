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
  SECRET_KEY,
  SERVER_IP,
  SERVER_PORT,
  EUREKA_DISABLED,
} from './src/libs/constants';
import fastifyRedis from '@fastify/redis';
import { currentAuthPlugin } from './src/plugin/authPlugin';
import { fastifySwagger } from '@fastify/swagger';
import fastifySwaggerUi from '@fastify/swagger-ui';
import { z } from 'zod';
import { hasZodFastifySchemaValidationErrors } from 'fastify-type-provider-zod';
import { handleError } from './src/libs/errorHelper';
import redis from './src/libs/redis';
import EurekaClient from './src/libs/eureka';

const eurekaConfig = {
  instance: {
    app: 'AUTH-SERVER',
    hostName: 'auth-server',
    ipAddr: 'auth-server',
    status: 'UP',
    port: {
      $: 9000,
      '@enabled': 'true',
    },
    vipAddress: 'auth-server',
    statusPageUrl: 'http://auth-server:9000/',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    },
  },
  eureka: {
    host: 'discovery-server',
    port: 8761,
    servicePath: '/eureka/apps',
  },
};

const eurekaClient = new EurekaClient(eurekaConfig);

eurekaClient.register();

const app = Fastify({
  logger: {
    level: 'debug',
    serializers: {
      req(request) {
        return {
          method: request.method,
          url: request.url,
          headers: request.headers,
          hostname: request.hostname,
          remoteAddress: request.ip,
          remotePort: request.socket.remotePort,
        };
      },
      err(error) {
        return {
          type: error.name,
          message: error.message,
          stack: error.stack || '',
        };
      },
    },
  },
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
    components: {
      securitySchemes: {
        bearerAuth: {
          type: 'apiKey',
          name: 'Authorization',
          in: 'header',
        },
      },
    },
    servers: [
      {
        url: 'http://localhost:9000',
        description: 'Local',
      },
      {
        url: `http://${SERVER_IP}:${SERVER_PORT}`,
        description: 'Auth Server',
      },
    ],
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

app.register(cors, {
  origin: ['http://localhost:5173', 'https://bbebig.netlify.app'],
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'Origin', 'Accept'],
  exposedHeaders: ['Set-Cookie'],
  preflightContinue: true,
  optionsSuccessStatus: 204,
});

app
  .register(fastifyRedis, {
    client: redis,
  })
  .ready((err) => {
    if (err) {
      console.error('Redis connection failed:', err);
    } else {
      console.log('Redis connected successfully');
    }
  });

app.register(fastifyCookie, {
  secret: SECRET_KEY,
  hook: 'onRequest',
  parseOptions: {
    secure: true,
    httpOnly: true,
    sameSite: 'none',
    path: '/',
  },
} as FastifyCookieOptions);

app.addHook('onRequest', async (request, reply) => {
  reply.header('Access-Control-Allow-Credentials', 'true');
  reply.header('Access-Control-Allow-Origin', request.headers.origin || '');
});

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

  if (err.name === 'JsonWebTokenError' || err.name === 'TokenExpiredError') {
    return reply.status(401).send({
      code: 'AUTH004',
      message: 'Token expired or invalid',
    });
  }

  return reply.status(401).send({
    code: err.code || 'AUTH004',
    message: err.message || 'Unauthorized',
  });
});

// 데이터베이스 연결 테스트 함수
// async function testDatabaseConnection() {
//   try {
//     console.log('Database URL:', {
//       url: process.env.DATABASE_URL?.replace(/\/\/.*:.*@/, '//[HIDDEN_CREDENTIALS]@'),
//     });

//     // 간단한 쿼리로 연결 테스트
//     await db.$queryRaw`SELECT 1+1 as result`;
//     console.log('✅ Database connection successful');

//     // 추가로 데이터베이스 정보 확인
//     const dbInfo = await db.$queryRaw`SELECT @@hostname, @@port, database()`;
//     console.log('Database Info:', dbInfo);
//   } catch (error) {
//     console.error('❌ Database connection failed:', error);
//     throw error;
//   }
// }

const start = async () => {
  try {
    // await testDatabaseConnection();

    await app.listen({ port: Number(SERVER_PORT), host: '0.0.0.0' });
    console.log(`listening on port ${SERVER_PORT}`);
  } catch (error) {
    app.log.error(error);
    eurekaClient.deregister();
    process.exit(1);
  }
};

start();
