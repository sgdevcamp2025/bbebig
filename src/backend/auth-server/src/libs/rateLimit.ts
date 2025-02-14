import { ERROR_MESSAGE, REDIS_KEY } from './constants';
import redis from './redis';

const RATE_LIMIT = {
  MAX_ATTEMPTS: 5,
  WINDOW: 15 * 60,
  BLOCK_DURATION: 3600,
} as const;

export const checkRateLimit = async (identifier: string) => {
  const key = REDIS_KEY.rateLimit(identifier);
  const attempts = await redis.incr(key);

  if (attempts === 1) {
    await redis.expire(key, RATE_LIMIT.WINDOW);
  }

  const isBlocked = await redis.get(REDIS_KEY.blocked(identifier));
  if (isBlocked) {
    const ttl = await redis.ttl(REDIS_KEY.blocked(identifier));
    throw {
      ...ERROR_MESSAGE.tooManyRequests,
      message: `너무 많은 시도입니다. ${Math.ceil(ttl / 60)}분 후에 다시 시도해주세요.`,
    };
  }

  if (attempts > RATE_LIMIT.MAX_ATTEMPTS) {
    await redis.setex(REDIS_KEY.blocked(identifier), RATE_LIMIT.BLOCK_DURATION, '1');
    await redis.del(key);

    throw ERROR_MESSAGE.tooManyRequests;
  }

  return attempts;
};
