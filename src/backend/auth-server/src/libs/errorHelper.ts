import { FastifyReply } from 'fastify';

function handleError(res: FastifyReply, errorType: { code: string; message: string }, error?: any) {
  res.log.error(error);
  res.status(400).send(errorType);
}

export { handleError };
