import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sede from './sede';
import SedeDetail from './sede-detail';
import SedeUpdate from './sede-update';
import SedeDeleteDialog from './sede-delete-dialog';

const SedeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sede />} />
    <Route path="new" element={<SedeUpdate />} />
    <Route path=":id">
      <Route index element={<SedeDetail />} />
      <Route path="edit" element={<SedeUpdate />} />
      <Route path="delete" element={<SedeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SedeRoutes;
