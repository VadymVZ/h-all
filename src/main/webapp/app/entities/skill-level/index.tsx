import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SkillLevel from './skill-level';
import SkillLevelDetail from './skill-level-detail';
import SkillLevelUpdate from './skill-level-update';
import SkillLevelDeleteDialog from './skill-level-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SkillLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SkillLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SkillLevelDetail} />
      <ErrorBoundaryRoute path={match.url} component={SkillLevel} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SkillLevelDeleteDialog} />
  </>
);

export default Routes;
