export interface IEmployeeAccount {
  id?: number;
  firstName?: string;
  lastName?: string;
  age?: number;
}

export const defaultValue: Readonly<IEmployeeAccount> = {};
