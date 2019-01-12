export interface IProfile {
  id?: number;
  salaryAmount?: number;
  city?: string;
  description?: string;
  experience?: number;
  jobExpectations?: string;
  achievements?: string;
  positionName?: string;
  yContentType?: string;
  y?: any;
}

export const defaultValue: Readonly<IProfile> = {};
