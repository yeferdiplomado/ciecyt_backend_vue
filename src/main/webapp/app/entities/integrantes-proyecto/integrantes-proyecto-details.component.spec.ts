/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import IntegrantesProyectoDetails from './integrantes-proyecto-details.vue';
import IntegrantesProyectoService from './integrantes-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type IntegrantesProyectoDetailsComponentType = InstanceType<typeof IntegrantesProyectoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const integrantesProyectoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('IntegrantesProyecto Management Detail Component', () => {
    let integrantesProyectoServiceStub: SinonStubbedInstance<IntegrantesProyectoService>;
    let mountOptions: MountingOptions<IntegrantesProyectoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      integrantesProyectoServiceStub = sinon.createStubInstance<IntegrantesProyectoService>(IntegrantesProyectoService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          integrantesProyectoService: () => integrantesProyectoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        integrantesProyectoServiceStub.find.resolves(integrantesProyectoSample);
        route = {
          params: {
            integrantesProyectoId: '' + 123,
          },
        };
        const wrapper = shallowMount(IntegrantesProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.integrantesProyecto).toMatchObject(integrantesProyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        integrantesProyectoServiceStub.find.resolves(integrantesProyectoSample);
        const wrapper = shallowMount(IntegrantesProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
