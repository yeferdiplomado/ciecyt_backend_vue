/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProyectoUpdate from './proyecto-update.vue';
import ProyectoService from './proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type ProyectoUpdateComponentType = InstanceType<typeof ProyectoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const proyectoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProyectoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Proyecto Management Update Component', () => {
    let comp: ProyectoUpdateComponentType;
    let proyectoServiceStub: SinonStubbedInstance<ProyectoService>;

    beforeEach(() => {
      route = {};
      proyectoServiceStub = sinon.createStubInstance<ProyectoService>(ProyectoService);
      proyectoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          proyectoService: () => proyectoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.proyecto = proyectoSample;
        proyectoServiceStub.update.resolves(proyectoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proyectoServiceStub.update.calledWith(proyectoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        proyectoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.proyecto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(proyectoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        proyectoServiceStub.find.resolves(proyectoSample);
        proyectoServiceStub.retrieve.resolves([proyectoSample]);

        // WHEN
        route = {
          params: {
            proyectoId: '' + proyectoSample.id,
          },
        };
        const wrapper = shallowMount(ProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.proyecto).toMatchObject(proyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        proyectoServiceStub.find.resolves(proyectoSample);
        const wrapper = shallowMount(ProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
