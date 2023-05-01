import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocumentoReingresoEstudiante from './documento-reingreso-estudiante';
import DocumentoReingresoEstudianteDetail from './documento-reingreso-estudiante-detail';
import DocumentoReingresoEstudianteUpdate from './documento-reingreso-estudiante-update';
import DocumentoReingresoEstudianteDeleteDialog from './documento-reingreso-estudiante-delete-dialog';

const DocumentoReingresoEstudianteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocumentoReingresoEstudiante />} />
    <Route path="new" element={<DocumentoReingresoEstudianteUpdate />} />
    <Route path=":id">
      <Route index element={<DocumentoReingresoEstudianteDetail />} />
      <Route path="edit" element={<DocumentoReingresoEstudianteUpdate />} />
      <Route path="delete" element={<DocumentoReingresoEstudianteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentoReingresoEstudianteRoutes;
