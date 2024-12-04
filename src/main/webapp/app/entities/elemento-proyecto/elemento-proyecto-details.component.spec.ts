/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ElementoProyectoDetails from './elemento-proyecto-details.vue';
import ElementoProyectoService from './elemento-proyecto.service';
import AlertService from '@/shared/alert/alert.service';

type ElementoProyectoDetailsComponentType = InstanceType<typeof ElementoProyectoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const elementoProyectoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ElementoProyecto Management Detail Component', () => {
    let elementoProyectoServiceStub: SinonStubbedInstance<ElementoProyectoService>;
    let mountOptions: MountingOptions<ElementoProyectoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      elementoProyectoServiceStub = sinon.createStubInstance<ElementoProyectoService>(ElementoProyectoService);

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
          elementoProyectoService: () => elementoProyectoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        elementoProyectoServiceStub.find.resolves(elementoProyectoSample);
        route = {
          params: {
            elementoProyectoId: '' + 123,
          },
        };
        const wrapper = shallowMount(ElementoProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.elementoProyecto).toMatchObject(elementoProyectoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        elementoProyectoServiceStub.find.resolves(elementoProyectoSample);
        const wrapper = shallowMount(ElementoProyectoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
