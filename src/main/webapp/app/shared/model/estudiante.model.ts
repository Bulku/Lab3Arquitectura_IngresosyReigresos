import dayjs from 'dayjs';

export interface IEstudiante {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  fechaNacimiento?: string | null;
  correo?: string | null;
  direccion?: string | null;
}

export const defaultValue: Readonly<IEstudiante> = {};
