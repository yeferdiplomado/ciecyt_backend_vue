import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ProyectoService from './proyecto.service';
import { type IProyecto } from '@/shared/model/proyecto.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProyectoDetails',
  setup() {
    const proyectoService = inject('proyectoService', () => new ProyectoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const proyecto: Ref<IProyecto> = ref({});

    const retrieveProyecto = async proyectoId => {
      try {
        const res = await proyectoService().find(proyectoId);
        proyecto.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.proyectoId) {
      retrieveProyecto(route.params.proyectoId);
    }

    return {
      alertService,
      proyecto,

      previousState,
      t$: useI18n().t,
    };
  },
});
