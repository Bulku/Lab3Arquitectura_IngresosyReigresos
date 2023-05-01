import { IMateria } from 'app/shared/model/materia.model';

export interface IPensum {
  id?: number;
  numero?: number | null;
  materias?: IMateria[] | null;
}

export const defaultValue: Readonly<IPensum> = {};
