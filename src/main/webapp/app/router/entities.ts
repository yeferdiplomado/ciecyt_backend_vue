import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Elemento = () => import('@/entities/elemento/elemento.vue');
const ElementoUpdate = () => import('@/entities/elemento/elemento-update.vue');
const ElementoDetails = () => import('@/entities/elemento/elemento-details.vue');

const ElementoProyecto = () => import('@/entities/elemento-proyecto/elemento-proyecto.vue');
const ElementoProyectoUpdate = () => import('@/entities/elemento-proyecto/elemento-proyecto-update.vue');
const ElementoProyectoDetails = () => import('@/entities/elemento-proyecto/elemento-proyecto-details.vue');

const IntegrantesProyecto = () => import('@/entities/integrantes-proyecto/integrantes-proyecto.vue');
const IntegrantesProyectoUpdate = () => import('@/entities/integrantes-proyecto/integrantes-proyecto-update.vue');
const IntegrantesProyectoDetails = () => import('@/entities/integrantes-proyecto/integrantes-proyecto-details.vue');

const Proyecto = () => import('@/entities/proyecto/proyecto.vue');
const ProyectoUpdate = () => import('@/entities/proyecto/proyecto-update.vue');
const ProyectoDetails = () => import('@/entities/proyecto/proyecto-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'elemento',
      name: 'Elemento',
      component: Elemento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento/new',
      name: 'ElementoCreate',
      component: ElementoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento/:elementoId/edit',
      name: 'ElementoEdit',
      component: ElementoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento/:elementoId/view',
      name: 'ElementoView',
      component: ElementoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento-proyecto',
      name: 'ElementoProyecto',
      component: ElementoProyecto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento-proyecto/new',
      name: 'ElementoProyectoCreate',
      component: ElementoProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento-proyecto/:elementoProyectoId/edit',
      name: 'ElementoProyectoEdit',
      component: ElementoProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'elemento-proyecto/:elementoProyectoId/view',
      name: 'ElementoProyectoView',
      component: ElementoProyectoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'integrantes-proyecto',
      name: 'IntegrantesProyecto',
      component: IntegrantesProyecto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'integrantes-proyecto/new',
      name: 'IntegrantesProyectoCreate',
      component: IntegrantesProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'integrantes-proyecto/:integrantesProyectoId/edit',
      name: 'IntegrantesProyectoEdit',
      component: IntegrantesProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'integrantes-proyecto/:integrantesProyectoId/view',
      name: 'IntegrantesProyectoView',
      component: IntegrantesProyectoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto',
      name: 'Proyecto',
      component: Proyecto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/new',
      name: 'ProyectoCreate',
      component: ProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/:proyectoId/edit',
      name: 'ProyectoEdit',
      component: ProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/:proyectoId/view',
      name: 'ProyectoView',
      component: ProyectoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
