import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Occupation from './occupation';
import OccupationDetail from './occupation-detail';
import OccupationUpdate from './occupation-update';
import OccupationDeleteDialog from './occupation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OccupationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OccupationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OccupationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Occupation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OccupationDeleteDialog} />
  </>
);

export default Routes;
