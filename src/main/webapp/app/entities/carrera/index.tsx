import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Carrera from './carrera';
import CarreraDetail from './carrera-detail';
import CarreraUpdate from './carrera-update';
import CarreraDeleteDialog from './carrera-delete-dialog';

const CarreraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Carrera />} />
    <Route path="new" element={<CarreraUpdate />} />
    <Route path=":id">
      <Route index element={<CarreraDetail />} />
      <Route path="edit" element={<CarreraUpdate />} />
      <Route path="delete" element={<CarreraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CarreraRoutes;
