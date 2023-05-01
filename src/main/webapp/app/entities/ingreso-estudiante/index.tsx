import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IngresoEstudiante from './ingreso-estudiante';
import IngresoEstudianteDetail from './ingreso-estudiante-detail';
import IngresoEstudianteUpdate from './ingreso-estudiante-update';
import IngresoEstudianteDeleteDialog from './ingreso-estudiante-delete-dialog';

const IngresoEstudianteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IngresoEstudiante />} />
    <Route path="new" element={<IngresoEstudianteUpdate />} />
    <Route path=":id">
      <Route index element={<IngresoEstudianteDetail />} />
      <Route path="edit" element={<IngresoEstudianteUpdate />} />
      <Route path="delete" element={<IngresoEstudianteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IngresoEstudianteRoutes;
