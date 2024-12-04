import { type IElemento } from '@/shared/model/elemento.model';

export interface IElementoProyecto {
  id?: number;
  dato?: string | null;
  descripcion?: string | null;
  elemento?: IElemento | null;
}

export class ElementoProyecto implements IElementoProyecto {
  constructor(
    public id?: number,
    public dato?: string | null,
    public descripcion?: string | null,
    public elemento?: IElemento | null,
  ) {}
}
