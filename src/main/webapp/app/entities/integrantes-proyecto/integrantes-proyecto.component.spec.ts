/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import IntegrantesProyecto from './integrantes-proyecto.vue';
import IntegrantesProyectoService from './integrantes-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type IntegrantesProyectoComponentType = InstanceType<typeof IntegrantesProyecto>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('IntegrantesProyecto Management Component', () => {
    let integrantesProyectoServiceStub: SinonStubbedInstance<IntegrantesProyectoService>;
    let mountOptions: MountingOptions<IntegrantesProyectoComponentType>['global'];

    beforeEach(() => {
      integrantesProyectoServiceStub = sinon.createStubInstance<IntegrantesProyectoService>(IntegrantesProyectoService);
      integrantesProyectoServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          integrantesProyectoService: () => integrantesProyectoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        integrantesProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(IntegrantesProyecto, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.integrantesProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(IntegrantesProyecto, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: IntegrantesProyectoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(IntegrantesProyecto, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        integrantesProyectoServiceStub.retrieve.reset();
        integrantesProyectoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        integrantesProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.retrieve.called).toBeTruthy();
        expect(comp.integrantesProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(integrantesProyectoServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        integrantesProyectoServiceStub.retrieve.reset();
        integrantesProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(integrantesProyectoServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.integrantesProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        integrantesProyectoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeIntegrantesProyecto();
        await comp.$nextTick(); // clear components

        // THEN
        expect(integrantesProyectoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(integrantesProyectoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
