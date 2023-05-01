import { IPensum } from 'app/shared/model/pensum.model';

export interface IMateria {
  id?: number;
  nombre?: string | null;
  creditos?: number | null;
  pensums?: IPensum[] | null;
}

export const defaultValue: Readonly<IMateria> = {};
