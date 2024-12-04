/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import IntegrantesProyectoUpdate from './integrantes-proyecto-update.vue';
import IntegrantesProyectoService from './integrantes-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';

type IntegrantesProyectoUpdateComponentType = InstanceType<typeof IntegrantesProyectoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const integrantesProyectoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<IntegrantesProyectoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('IntegrantesProyecto Management Update Component', () => {
    let comp: IntegrantesProyectoUpdateComponentType;
    let integrantesProyectoServiceStub: SinonStubbedInstance<IntegrantesProyectoService>;

    beforeEach(() => {
      route = {};
      integrantesProyectoServiceStub = sinon.createStubInstance<IntegrantesProyectoService>(IntegrantesProyectoService);
      integrantesProyectoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          integrantesProyectoService: () => integrantesProyectoServiceStub,
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
        const wrapper = shallowMount(IntegrantesProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.integrantesProyecto = integrantesProyectoSample;
        integrantesProyectoServiceStub.update.resolves(integrantesProyectoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.update.calledWith(integrantesProyectoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        integrantesProyectoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(IntegrantesProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.integrantesProyecto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(integrantesProyectoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        integrantesProyectoServiceStub.find.resolves(integrantesProyectoSample);
        integrantesProyectoServiceStub.retrieve.resolves([integrantesProyectoSample]);

        // WHEN
        route = {
          params: {
            integrantesProyectoId: '' + integrantesProyectoSample.id,
          },
        };
        const wrapper = shallowMount(IntegrantesProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.integrantesProyecto).toMatchObject(integrantesProyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        integrantesProyectoServiceStub.find.resolves(integrantesProyectoSample);
        const wrapper = shallowMount(IntegrantesProyectoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
