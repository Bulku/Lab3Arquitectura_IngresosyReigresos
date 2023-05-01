import { ISolicitudReingreso } from 'app/shared/model/solicitud-reingreso.model';

export interface IDocumentoReingresoEstudiante {
  id?: number;
  nombreDocumento?: string | null;
  descripcionDocumento?: string | null;
  solicitudReingreso?: ISolicitudReingreso | null;
}

export const defaultValue: Readonly<IDocumentoReingresoEstudiante> = {};
