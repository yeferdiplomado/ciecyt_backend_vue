import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ElementoService from './elemento.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';
import { type IProyecto } from '@/shared/model/proyecto.model';
import { type IElemento, Elemento } from '@/shared/model/elemento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ElementoUpdate',
  setup() {
    const elementoService = inject('elementoService', () => new ElementoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const elemento: Ref<IElemento> = ref(new Elemento());

    const proyectoService = inject('proyectoService', () => new ProyectoService());

    const proyectos: Ref<IProyecto[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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
      elemento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
      proyecto: {},
    };
    const v$ = useVuelidate(validationRules, elemento as any);
    v$.value.$validate();

    return {
      elementoService,
      alertService,
      elemento,
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
      if (this.elemento.id) {
        this.elementoService()
          .update(this.elemento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('ciecytApp.elemento.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.elementoService()
          .create(this.elemento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('ciecytApp.elemento.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
