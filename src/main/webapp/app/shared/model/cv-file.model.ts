export interface ICvFile {
  id?: number;
  cvContentType?: string;
  cv?: any;
}

export const defaultValue: Readonly<ICvFile> = {};
