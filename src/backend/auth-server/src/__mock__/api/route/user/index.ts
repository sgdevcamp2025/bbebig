import { FastifyInstance } from 'fastify';
import { ZodTypeProvider } from 'fastify-type-provider-zod';
import userList from '@/__mock__/api/data/user';
import { loginSchema, logoutSchema, signupSchema } from '../../schema/userSchema';

const userRoute = async (app: FastifyInstance) => {
  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/login',
    schema: loginSchema,
    handler: (req, res) => {
      const user = userList.get(req.body.email);
      if (!user) {
        res.status(404).send({ status: 404, success: false, message: 'User not found' });
      }
      res.send({ status: 200, success: true, message: 'Login successful' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/signup',
    schema: signupSchema,
    handler: (req, res) => {
      const user = userList.get(req.body.email);
      if (user) {
        res.status(400).send({ status: 400, success: false, message: 'User already exists' });
      }
      userList.set(req.body.email, { id: '1', email: req.body.email, password: req.body.password });
      res.send({ status: 201, success: true, message: 'Signup successful' });
    },
  });

  app.withTypeProvider<ZodTypeProvider>().route({
    method: 'POST',
    url: '/logout',
    schema: logoutSchema,
    handler: async (req, res) => {
      res.clearCookie('refreshToken', { path: '/' });
      res.send({ status: 205, success: true, message: 'Logout successful' });
    },
  });
};

export default userRoute;
