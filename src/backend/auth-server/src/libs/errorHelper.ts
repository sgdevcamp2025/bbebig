import { FastifyReply } from 'fastify';

function handleError(res: FastifyReply, errorType: { code: number; message: string }, error?: any) {
  res.log.error(error);
  res.status(errorType.code).send(errorType);
}

export { handleError };
