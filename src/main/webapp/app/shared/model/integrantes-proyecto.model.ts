import { type IProyecto } from '@/shared/model/proyecto.model';

export interface IIntegrantesProyecto {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  proyecto?: IProyecto | null;
}

export class IntegrantesProyecto implements IIntegrantesProyecto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
    public proyecto?: IProyecto | null,
  ) {}
}
