import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmployeeAccount from './employee-account';
import Skill from './skill';
import SkillLevel from './skill-level';
import Profile from './profile';
import Photo from './photo';
import StopWord from './stop-word';
import Phone from './phone';
import Occupation from './occupation';
import LanguageLevel from './language-level';
import Language from './language';
import JobCategory from './job-category';
import CvFile from './cv-file';
import Contact from './contact';
import UserAccount from './user-account';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/employee-account`} component={EmployeeAccount} />
      <ErrorBoundaryRoute path={`${match.url}/skill`} component={Skill} />
      <ErrorBoundaryRoute path={`${match.url}/skill-level`} component={SkillLevel} />
      <ErrorBoundaryRoute path={`${match.url}/profile`} component={Profile} />
      <ErrorBoundaryRoute path={`${match.url}/photo`} component={Photo} />
      <ErrorBoundaryRoute path={`${match.url}/stop-word`} component={StopWord} />
      <ErrorBoundaryRoute path={`${match.url}/phone`} component={Phone} />
      <ErrorBoundaryRoute path={`${match.url}/occupation`} component={Occupation} />
      <ErrorBoundaryRoute path={`${match.url}/language-level`} component={LanguageLevel} />
      <ErrorBoundaryRoute path={`${match.url}/language`} component={Language} />
      <ErrorBoundaryRoute path={`${match.url}/job-category`} component={JobCategory} />
      <ErrorBoundaryRoute path={`${match.url}/cv-file`} component={CvFile} />
      <ErrorBoundaryRoute path={`${match.url}/contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}/user-account`} component={UserAccount} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
