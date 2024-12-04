export interface IProyecto {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export class Proyecto implements IProyecto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
  ) {}
}
