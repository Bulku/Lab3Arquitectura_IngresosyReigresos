import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estudiante from './estudiante';
import SolicitudReingreso from './solicitud-reingreso';
import DocumentoReingresoEstudiante from './documento-reingreso-estudiante';
import Carrera from './carrera';
import Pensum from './pensum';
import Materia from './materia';
import Sede from './sede';
import IngresoEstudiante from './ingreso-estudiante';
import DocumentoIngresoEstudiante from './documento-ingreso-estudiante';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="estudiante/*" element={<Estudiante />} />
        <Route path="solicitud-reingreso/*" element={<SolicitudReingreso />} />
        <Route path="documento-reingreso-estudiante/*" element={<DocumentoReingresoEstudiante />} />
        <Route path="carrera/*" element={<Carrera />} />
        <Route path="pensum/*" element={<Pensum />} />
        <Route path="materia/*" element={<Materia />} />
        <Route path="sede/*" element={<Sede />} />
        <Route path="ingreso-estudiante/*" element={<IngresoEstudiante />} />
        <Route path="documento-ingreso-estudiante/*" element={<DocumentoIngresoEstudiante />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
