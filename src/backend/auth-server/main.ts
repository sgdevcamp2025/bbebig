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
import { ERROR_MESSAGE, SERVER_IP, SECRET_KEY, SERVER_PORT } from './src/libs/constants';
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
    homePageUrl: 'http://auth-server:9000/',
    statusPageUrl: 'http://auth-server:9000/',
    healthCheckUrl: 'http://auth-server:9000/',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$AmazonInfo',
      name: 'Amazon',
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
    client: redis,
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
