import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LanguageLevel from './language-level';
import LanguageLevelDetail from './language-level-detail';
import LanguageLevelUpdate from './language-level-update';
import LanguageLevelDeleteDialog from './language-level-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LanguageLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LanguageLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LanguageLevelDetail} />
      <ErrorBoundaryRoute path={match.url} component={LanguageLevel} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LanguageLevelDeleteDialog} />
  </>
);

export default Routes;
