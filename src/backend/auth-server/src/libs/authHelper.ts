import jwt from 'jsonwebtoken';
import { ACCESS_TOKEN_EXPIRES, REFRESH_TOKEN_EXPIRES, SECRET_KEY } from './constants';

const generateAccessToken = (user: { id: number; email: string }) => {
  const accessToken = jwt.sign({ id: user.id, email: user.email }, SECRET_KEY, {
    expiresIn: ACCESS_TOKEN_EXPIRES,
  });
  return accessToken;
};

const generateRefreshToken = (user: { id: number; email: string }) => {
  const refreshToken = jwt.sign({ id: user.id, email: user.email }, SECRET_KEY, {
    expiresIn: REFRESH_TOKEN_EXPIRES,
  });
  return refreshToken;
};
