import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import DOMPurify from 'dompurify';

import ElementoProyectoService from './elemento-proyecto.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IElementoProyecto } from '@/shared/model/elemento-proyecto.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ElementoProyectoDetails',
  setup() {
    const elementoProyectoService = inject('elementoProyectoService', () => new ElementoProyectoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const elementoProyecto: Ref<IElementoProyecto> = ref({});

    const retrieveElementoProyecto = async elementoProyectoId => {
      try {
        const res = await elementoProyectoService().find(elementoProyectoId);
        elementoProyecto.value = res;
        elementoProyecto.value.dato = DOMPurify.sanitize(elementoProyecto.value.dato ?? '');
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.elementoProyectoId) {
      retrieveElementoProyecto(route.params.elementoProyectoId);
    }

    return {
      alertService,
      elementoProyecto,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
