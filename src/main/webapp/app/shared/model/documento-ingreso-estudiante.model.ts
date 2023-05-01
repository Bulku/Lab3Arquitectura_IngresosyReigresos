import { IIngresoEstudiante } from 'app/shared/model/ingreso-estudiante.model';

export interface IDocumentoIngresoEstudiante {
  id?: number;
  nombreDocumento?: string | null;
  descripcionDocumento?: string | null;
  ingresoEstudiante?: IIngresoEstudiante | null;
}

export const defaultValue: Readonly<IDocumentoIngresoEstudiante> = {};
