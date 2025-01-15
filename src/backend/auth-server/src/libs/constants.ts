import { Secret } from 'jsonwebtoken';

const FIRST_PWD = process.env.FIRST_PWD;
const ROUND = Number(process.env.ROUND);
const SECRET_KEY = process.env.SECRET_KEY as Secret;
const ACCESS_TOKEN_EXPIRES = process.env.ACCESS_TOKEN_EXPIRES;
const REFRESH_TOKEN_EXPIRES = process.env.REFRESH_TOKEN_EXPIRES;

export { FIRST_PWD, ROUND, SECRET_KEY, ACCESS_TOKEN_EXPIRES, REFRESH_TOKEN_EXPIRES };
