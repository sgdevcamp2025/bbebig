import { FastifyReply } from 'fastify';
import { CommonResponseType } from 'src/schema/type';

function handleSuccess(res: FastifyReply, data: CommonResponseType, callback?: () => void) {
  res.log.info(data);
  res.status(200).send({
    data,
  });
  callback?.();
}

export default handleSuccess;
