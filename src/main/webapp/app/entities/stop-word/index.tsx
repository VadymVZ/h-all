import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StopWord from './stop-word';
import StopWordDetail from './stop-word-detail';
import StopWordUpdate from './stop-word-update';
import StopWordDeleteDialog from './stop-word-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StopWordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StopWordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StopWordDetail} />
      <ErrorBoundaryRoute path={match.url} component={StopWord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StopWordDeleteDialog} />
  </>
);

export default Routes;
