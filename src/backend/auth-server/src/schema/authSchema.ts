import { z } from 'zod';

const refreshTokenSchema = {
  response: {
    201: z.object({
      refreshToken: z.string(),
    }),
  },
};

export { refreshTokenSchema };
