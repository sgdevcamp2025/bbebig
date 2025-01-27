import { FastifyReply } from 'fastify';
import { CommonResponseType } from 'src/schema/type';

function handleSuccess(
  res: FastifyReply,
  data: CommonResponseType,
  callback?: (res: FastifyReply) => void,
) {
  res.status(200).send(data);
  callback?.(res);
}

export default handleSuccess;
