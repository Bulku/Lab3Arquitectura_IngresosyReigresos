import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocumentoIngresoEstudiante from './documento-ingreso-estudiante';
import DocumentoIngresoEstudianteDetail from './documento-ingreso-estudiante-detail';
import DocumentoIngresoEstudianteUpdate from './documento-ingreso-estudiante-update';
import DocumentoIngresoEstudianteDeleteDialog from './documento-ingreso-estudiante-delete-dialog';

const DocumentoIngresoEstudianteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocumentoIngresoEstudiante />} />
    <Route path="new" element={<DocumentoIngresoEstudianteUpdate />} />
    <Route path=":id">
      <Route index element={<DocumentoIngresoEstudianteDetail />} />
      <Route path="edit" element={<DocumentoIngresoEstudianteUpdate />} />
      <Route path="delete" element={<DocumentoIngresoEstudianteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentoIngresoEstudianteRoutes;
