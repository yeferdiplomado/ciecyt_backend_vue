/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProyectoDetails from './proyecto-details.vue';
import ProyectoService from './proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type ProyectoDetailsComponentType = InstanceType<typeof ProyectoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const proyectoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Proyecto Management Detail Component', () => {
    let proyectoServiceStub: SinonStubbedInstance<ProyectoService>;
    let mountOptions: MountingOptions<ProyectoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      proyectoServiceStub = sinon.createStubInstance<ProyectoService>(ProyectoService);

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
          proyectoService: () => proyectoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        proyectoServiceStub.find.resolves(proyectoSample);
        route = {
          params: {
            proyectoId: '' + 123,
          },
        };
        const wrapper = shallowMount(ProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.proyecto).toMatchObject(proyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        proyectoServiceStub.find.resolves(proyectoSample);
        const wrapper = shallowMount(ProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
