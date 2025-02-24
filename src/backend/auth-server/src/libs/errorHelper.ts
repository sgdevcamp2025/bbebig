import { FastifyReply } from 'fastify';
import { CommonResponseType } from '../schema/type';

function handleError(res: FastifyReply, errorType: CommonResponseType, error?: any) {
  res.log.error(error);
  res.status(400).send(errorType);
}

export { handleError };
