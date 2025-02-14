import { FastifyReply } from 'fastify';
import { CommonResponseType } from 'src/schema/type';

function handleSuccess(
  res: FastifyReply,
  data: CommonResponseType,
  code: number = 200,
  callback?: (res: FastifyReply) => void,
) {
  res.status(code).send(data);
  callback?.(res);
}

export default handleSuccess;
