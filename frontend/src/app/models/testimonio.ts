export interface Testimonio {
  id: number;
  texto: string;
  autorNombre: string;
  autorUbicacion: string;
  estrellas: number;
}

export class TestimonioModel implements Testimonio {
  id: number = 0;
  texto: string = '';
  autorNombre: string = '';
  autorUbicacion: string = '';
  estrellas: number = 0;

  constructor(data?: Partial<Testimonio>) {
    Object.assign(this, data);
  }
}