import { Modalidad } from 'app/shared/model/enumerations/modalidad.model';

export interface ICarrera {
  id?: number;
  nombre?: string | null;
  modalidad?: Modalidad | null;
}

export const defaultValue: Readonly<ICarrera> = {};
