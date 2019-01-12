export interface IContact {
  id?: number;
  name?: string;
  lastName?: string;
  skype?: string;
  github?: string;
  telegram?: string;
  linkedin?: string;
  email?: string;
}

export const defaultValue: Readonly<IContact> = {};
