/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ElementoProyectoUpdate from './elemento-proyecto-update.vue';
import ElementoProyectoService from './elemento-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

import ElementoService from '@/entities/elemento/elemento.service';

type ElementoProyectoUpdateComponentType = InstanceType<typeof ElementoProyectoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const elementoProyectoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ElementoProyectoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ElementoProyecto Management Update Component', () => {
    let comp: ElementoProyectoUpdateComponentType;
    let elementoProyectoServiceStub: SinonStubbedInstance<ElementoProyectoService>;

    beforeEach(() => {
      route = {};
      elementoProyectoServiceStub = sinon.createStubInstance<ElementoProyectoService>(ElementoProyectoService);
      elementoProyectoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          elementoProyectoService: () => elementoProyectoServiceStub,
          elementoService: () =>
            sinon.createStubInstance<ElementoService>(ElementoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ElementoProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.elementoProyecto = elementoProyectoSample;
        elementoProyectoServiceStub.update.resolves(elementoProyectoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.update.calledWith(elementoProyectoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        elementoProyectoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ElementoProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.elementoProyecto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementoProyectoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        elementoProyectoServiceStub.find.resolves(elementoProyectoSample);
        elementoProyectoServiceStub.retrieve.resolves([elementoProyectoSample]);

        // WHEN
        route = {
          params: {
            elementoProyectoId: '' + elementoProyectoSample.id,
          },
        };
        const wrapper = shallowMount(ElementoProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.elementoProyecto).toMatchObject(elementoProyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        elementoProyectoServiceStub.find.resolves(elementoProyectoSample);
        const wrapper = shallowMount(ElementoProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
