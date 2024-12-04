/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ElementoProyecto from './elemento-proyecto.vue';
import ElementoProyectoService from './elemento-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type ElementoProyectoComponentType = InstanceType<typeof ElementoProyecto>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ElementoProyecto Management Component', () => {
    let elementoProyectoServiceStub: SinonStubbedInstance<ElementoProyectoService>;
    let mountOptions: MountingOptions<ElementoProyectoComponentType>['global'];

    beforeEach(() => {
      elementoProyectoServiceStub = sinon.createStubInstance<ElementoProyectoService>(ElementoProyectoService);
      elementoProyectoServiceStub.retrieve.resolves({ headers: {} });

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
          elementoProyectoService: () => elementoProyectoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        elementoProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ElementoProyecto, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.elementoProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ElementoProyecto, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ElementoProyectoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ElementoProyecto, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        elementoProyectoServiceStub.retrieve.reset();
        elementoProyectoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        elementoProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.retrieve.called).toBeTruthy();
        expect(comp.elementoProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(elementoProyectoServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        elementoProyectoServiceStub.retrieve.reset();
        elementoProyectoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(elementoProyectoServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.elementoProyectos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        elementoProyectoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeElementoProyecto();
        await comp.$nextTick(); // clear components

        // THEN
        expect(elementoProyectoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(elementoProyectoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
