import estudiante from 'app/entities/estudiante/estudiante.reducer';
import solicitudReingreso from 'app/entities/solicitud-reingreso/solicitud-reingreso.reducer';
import documentoReingresoEstudiante from 'app/entities/documento-reingreso-estudiante/documento-reingreso-estudiante.reducer';
import carrera from 'app/entities/carrera/carrera.reducer';
import pensum from 'app/entities/pensum/pensum.reducer';
import materia from 'app/entities/materia/materia.reducer';
import sede from 'app/entities/sede/sede.reducer';
import ingresoEstudiante from 'app/entities/ingreso-estudiante/ingreso-estudiante.reducer';
import documentoIngresoEstudiante from 'app/entities/documento-ingreso-estudiante/documento-ingreso-estudiante.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  estudiante,
  solicitudReingreso,
  documentoReingresoEstudiante,
  carrera,
  pensum,
  materia,
  sede,
  ingresoEstudiante,
  documentoIngresoEstudiante,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
