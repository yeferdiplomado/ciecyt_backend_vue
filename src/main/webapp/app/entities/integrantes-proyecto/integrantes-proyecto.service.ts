import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IIntegrantesProyecto } from '@/shared/model/integrantes-proyecto.model';

const baseApiUrl = 'api/integrantes-proyectos';

export default class IntegrantesProyectoService {
  public find(id: number): Promise<IIntegrantesProyecto> {
    return new Promise<IIntegrantesProyecto>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl + `?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IIntegrantesProyecto): Promise<IIntegrantesProyecto> {
    return new Promise<IIntegrantesProyecto>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IIntegrantesProyecto): Promise<IIntegrantesProyecto> {
    return new Promise<IIntegrantesProyecto>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IIntegrantesProyecto): Promise<IIntegrantesProyecto> {
    return new Promise<IIntegrantesProyecto>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
