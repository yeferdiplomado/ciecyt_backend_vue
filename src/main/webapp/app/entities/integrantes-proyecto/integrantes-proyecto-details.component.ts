import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import IntegrantesProyectoService from './integrantes-proyecto.service';
import { type IIntegrantesProyecto } from '@/shared/model/integrantes-proyecto.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'IntegrantesProyectoDetails',
  setup() {
    const integrantesProyectoService = inject('integrantesProyectoService', () => new IntegrantesProyectoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const integrantesProyecto: Ref<IIntegrantesProyecto> = ref({});

    const retrieveIntegrantesProyecto = async integrantesProyectoId => {
      try {
        const res = await integrantesProyectoService().find(integrantesProyectoId);
        integrantesProyecto.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.integrantesProyectoId) {
      retrieveIntegrantesProyecto(route.params.integrantesProyectoId);
    }

    return {
      alertService,
      integrantesProyecto,

      previousState,
      t$: useI18n().t,
    };
  },
});
