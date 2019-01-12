export interface IUserAccount {
  id?: number;
  activated?: boolean;
  recruiter?: boolean;
  receiveMailing?: boolean;
  name?: string;
}

export const defaultValue: Readonly<IUserAccount> = {
  activated: false,
  recruiter: false,
  receiveMailing: false
};
