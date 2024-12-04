import { type IProyecto } from '@/shared/model/proyecto.model';

export interface IElemento {
  id?: number;
  elemento?: string;
  descripcion?: string | null;
  proyecto?: IProyecto | null;
}

export class Elemento implements IElemento {
  constructor(
    public id?: number,
    public elemento?: string,
    public descripcion?: string | null,
    public proyecto?: IProyecto | null,
  ) {}
}
