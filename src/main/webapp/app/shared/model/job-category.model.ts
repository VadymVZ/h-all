export interface IJobCategory {
  id?: number;
  parentId?: number;
  name?: string;
}

export const defaultValue: Readonly<IJobCategory> = {};
