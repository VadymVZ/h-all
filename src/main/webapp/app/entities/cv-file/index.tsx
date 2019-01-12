import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CvFile from './cv-file';
import CvFileDetail from './cv-file-detail';
import CvFileUpdate from './cv-file-update';
import CvFileDeleteDialog from './cv-file-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CvFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CvFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CvFileDetail} />
      <ErrorBoundaryRoute path={match.url} component={CvFile} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CvFileDeleteDialog} />
  </>
);

export default Routes;
