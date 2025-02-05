import { z } from 'zod';
import { commonResponseSchema } from '../commonSchema';

export type CommonResponseType = z.infer<typeof commonResponseSchema>;
