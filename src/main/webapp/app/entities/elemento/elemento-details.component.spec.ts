/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ElementoDetails from './elemento-details.vue';
import ElementoService from './elemento.service';
import AlertService from '@/shared/alert/alert.service';

type ElementoDetailsComponentType = InstanceType<typeof ElementoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const elementoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Elemento Management Detail Component', () => {
    let elementoServiceStub: SinonStubbedInstance<ElementoService>;
    let mountOptions: MountingOptions<ElementoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      elementoServiceStub = sinon.createStubInstance<ElementoService>(ElementoService);

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
          elementoService: () => elementoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        elementoServiceStub.find.resolves(elementoSample);
        route = {
          params: {
            elementoId: '' + 123,
          },
        };
        const wrapper = shallowMount(ElementoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.elemento).toMatchObject(elementoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        elementoServiceStub.find.resolves(elementoSample);
        const wrapper = shallowMount(ElementoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
