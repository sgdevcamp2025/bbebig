import { z } from 'zod';
import { commonResponseSchema, commonResponseSchemaOmitResult } from '../commonSchema';

export type CommonResponseType = z.infer<typeof commonResponseSchema>;
export type CommonResponseTypeOmitResult = z.infer<typeof commonResponseSchemaOmitResult>;
