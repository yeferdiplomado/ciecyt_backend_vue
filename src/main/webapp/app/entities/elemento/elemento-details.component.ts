import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ElementoService from './elemento.service';
import { type IElemento } from '@/shared/model/elemento.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ElementoDetails',
  setup() {
    const elementoService = inject('elementoService', () => new ElementoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const elemento: Ref<IElemento> = ref({});

    const retrieveElemento = async elementoId => {
      try {
        const res = await elementoService().find(elementoId);
        elemento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.elementoId) {
      retrieveElemento(route.params.elementoId);
    }

    return {
      alertService,
      elemento,

      previousState,
      t$: useI18n().t,
    };
  },
});
