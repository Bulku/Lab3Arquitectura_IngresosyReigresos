import dayjs from 'dayjs';
import { IEstudiante } from 'app/shared/model/estudiante.model';

export interface ISolicitudReingreso {
  id?: number;
  fechaSolicitud?: string | null;
  motivo?: string | null;
  estudiante?: IEstudiante | null;
}

export const defaultValue: Readonly<ISolicitudReingreso> = {};
