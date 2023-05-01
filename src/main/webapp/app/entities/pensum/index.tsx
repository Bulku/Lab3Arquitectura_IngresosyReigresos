import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pensum from './pensum';
import PensumDetail from './pensum-detail';
import PensumUpdate from './pensum-update';
import PensumDeleteDialog from './pensum-delete-dialog';

const PensumRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pensum />} />
    <Route path="new" element={<PensumUpdate />} />
    <Route path=":id">
      <Route index element={<PensumDetail />} />
      <Route path="edit" element={<PensumUpdate />} />
      <Route path="delete" element={<PensumDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PensumRoutes;
