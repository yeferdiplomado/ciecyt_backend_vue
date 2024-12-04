import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ProyectoService from './proyecto.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IProyecto, Proyecto } from '@/shared/model/proyecto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProyectoUpdate',
  setup() {
    const proyectoService = inject('proyectoService', () => new ProyectoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const proyecto: Ref<IProyecto> = ref(new Proyecto());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
    };
    const v$ = useVuelidate(validationRules, proyecto as any);
    v$.value.$validate();

    return {
      proyectoService,
      alertService,
      proyecto,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.proyecto.id) {
        this.proyectoService()
          .update(this.proyecto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ciecytApp.proyecto.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.proyectoService()
          .create(this.proyecto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ciecytApp.proyecto.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
