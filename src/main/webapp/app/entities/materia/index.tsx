import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Materia from './materia';
import MateriaDetail from './materia-detail';
import MateriaUpdate from './materia-update';
import MateriaDeleteDialog from './materia-delete-dialog';

const MateriaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Materia />} />
    <Route path="new" element={<MateriaUpdate />} />
    <Route path=":id">
      <Route index element={<MateriaDetail />} />
      <Route path="edit" element={<MateriaUpdate />} />
      <Route path="delete" element={<MateriaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MateriaRoutes;
