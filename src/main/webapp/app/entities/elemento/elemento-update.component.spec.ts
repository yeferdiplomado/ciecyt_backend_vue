/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ElementoUpdate from './elemento-update.vue';
import ElementoService from './elemento.service';
import AlertService from '@/shared/alert/alert.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';

type ElementoUpdateComponentType = InstanceType<typeof ElementoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const elementoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ElementoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Elemento Management Update Component', () => {
    let comp: ElementoUpdateComponentType;
    let elementoServiceStub: SinonStubbedInstance<ElementoService>;

    beforeEach(() => {
      route = {};
      elementoServiceStub = sinon.createStubInstance<ElementoService>(ElementoService);
      elementoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          elementoService: () => elementoServiceStub,
          proyectoService: () =>
            sinon.createStubInstance<ProyectoService>(ProyectoService, {
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
        const wrapper = shallowMount(ElementoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.elemento = elementoSample;
        elementoServiceStub.update.resolves(elementoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementoServiceStub.update.calledWith(elementoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        elementoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ElementoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.elemento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        elementoServiceStub.find.resolves(elementoSample);
        elementoServiceStub.retrieve.resolves([elementoSample]);

        // WHEN
        route = {
          params: {
            elementoId: '' + elementoSample.id,
          },
        };
        const wrapper = shallowMount(ElementoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.elemento).toMatchObject(elementoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        elementoServiceStub.find.resolves(elementoSample);
        const wrapper = shallowMount(ElementoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
