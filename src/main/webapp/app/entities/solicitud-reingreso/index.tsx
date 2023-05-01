import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SolicitudReingreso from './solicitud-reingreso';
import SolicitudReingresoDetail from './solicitud-reingreso-detail';
import SolicitudReingresoUpdate from './solicitud-reingreso-update';
import SolicitudReingresoDeleteDialog from './solicitud-reingreso-delete-dialog';

const SolicitudReingresoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SolicitudReingreso />} />
    <Route path="new" element={<SolicitudReingresoUpdate />} />
    <Route path=":id">
      <Route index element={<SolicitudReingresoDetail />} />
      <Route path="edit" element={<SolicitudReingresoUpdate />} />
      <Route path="delete" element={<SolicitudReingresoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SolicitudReingresoRoutes;
