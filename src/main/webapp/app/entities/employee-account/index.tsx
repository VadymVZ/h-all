import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmployeeAccount from './employee-account';
import EmployeeAccountDetail from './employee-account-detail';
import EmployeeAccountUpdate from './employee-account-update';
import EmployeeAccountDeleteDialog from './employee-account-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmployeeAccountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmployeeAccountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmployeeAccountDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmployeeAccount} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EmployeeAccountDeleteDialog} />
  </>
);

export default Routes;
