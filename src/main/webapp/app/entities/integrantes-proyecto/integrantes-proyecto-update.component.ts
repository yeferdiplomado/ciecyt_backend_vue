import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import IntegrantesProyectoService from './integrantes-proyecto.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';
import { type IProyecto } from '@/shared/model/proyecto.model';
import { type IIntegrantesProyecto, IntegrantesProyecto } from '@/shared/model/integrantes-proyecto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'IntegrantesProyectoUpdate',
  setup() {
    const integrantesProyectoService = inject('integrantesProyectoService', () => new IntegrantesProyectoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const integrantesProyecto: Ref<IIntegrantesProyecto> = ref(new IntegrantesProyecto());

    const proyectoService = inject('proyectoService', () => new ProyectoService());

    const proyectos: Ref<IProyecto[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      proyectoService()
        .retrieve()
        .then(res => {
          proyectos.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
      proyecto: {},
    };
    const v$ = useVuelidate(validationRules, integrantesProyecto as any);
    v$.value.$validate();

    return {
      integrantesProyectoService,
      alertService,
      integrantesProyecto,
      previousState,
      isSaving,
      currentLanguage,
      proyectos,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.integrantesProyecto.id) {
        this.integrantesProyectoService()
          .update(this.integrantesProyecto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ciecytApp.integrantesProyecto.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.integrantesProyectoService()
          .create(this.integrantesProyecto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ciecytApp.integrantesProyecto.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
