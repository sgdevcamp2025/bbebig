import { FastifyInstance } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import { refreshTokenSchema, registerSchema, signInSchema } from '../../schema/authSchema';

import authService from '@/service/authService';
import { DOMAIN, ERROR_MESSAGE, SUCCESS_MESSAGE } from '@/libs/constants';
import { handleError } from '@/libs/errorHelper';

const authRoute = async (app: FastifyInstance) => {
  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/login',
    schema: signInSchema,
    handler: async (req, res) => {
      const { email, password } = req.body as { email: string; password: string };
      const values = await authService.loginWithPassword(email, password);

      res.setCookie('refreshToken', values.refreshToken, {
        domain: DOMAIN,
        sameSite: true,
        httpOnly: true,
        secure: true,
        path: '/',
        expires: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
      });

      const result = {
        email: values.email,
        accessToken: values.accessToken,
      };

      return {
        ...SUCCESS_MESSAGE.loginOk,
        details: result,
      };
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/register',
    schema: registerSchema,
    handler: async (req, res) => {
      const { email, password, name, nickname, birthdate } = req.body;

      try {
        await authService.register(email, password, name, nickname, birthdate);

        return SUCCESS_MESSAGE.registerOk;
      } catch (error) {
        handleError(res, ERROR_MESSAGE.serverError, error);
      }
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/logout',
    handler: (req, res) => {
      res.send({ message: 'Hello World' });
    },
  });

  // app.withTypeProvider<ZodTypeProvider>().route({
  //   method: 'POST',
  //   url: '/access',
  //   schema: accessTokenSchema,
  //   handler: (req, res) => {
  //     res.send({ accessToken: '123' });
  //   },
  // });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/refresh',
    schema: refreshTokenSchema,
    handler: async (req, res) => {
      const refresh_token = req.cookies.refresh_token;

      if (!refresh_token) {
        handleError(res, ERROR_MESSAGE.unauthorized);
        return;
      }
      // Todo: 토큰 검증 후 새로운 엑세스토큰 반환
      try {
        const values = await authService.refresh(refresh_token);
        // res.status(SUCCESS_MESSAGE.refreshToken.code).send({
        //   ...SUCCESS_MESSAGE.refreshToken,
        // });
      } catch (error) {
        handleError(res, ERROR_MESSAGE.unauthorized, error);
        return;
      }
    },
  });

  // Todo: 토큰 검증

  // app.withTypeProvider<ZodTypeProvider>().route({
  //   method: 'GET',
  //   url: '/check',
  //   handler: (req, res) => {
  //     res.send({ isSuccess: true });
  //   },
  // });
};

export default authRoute;
