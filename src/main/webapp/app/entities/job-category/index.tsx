import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JobCategory from './job-category';
import JobCategoryDetail from './job-category-detail';
import JobCategoryUpdate from './job-category-update';
import JobCategoryDeleteDialog from './job-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JobCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JobCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JobCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={JobCategory} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={JobCategoryDeleteDialog} />
  </>
);

export default Routes;
