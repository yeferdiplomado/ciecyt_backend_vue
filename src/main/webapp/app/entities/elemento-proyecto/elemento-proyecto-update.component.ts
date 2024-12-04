import { defineComponent, inject, onMounted, reactive, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
import Editor from '@tinymce/tinymce-vue';
import { useI18n } from 'vue-i18n';
import axios from 'axios';

import { type IElemento } from '@/shared/model/elemento.model';
import { type IElementoProyecto, ElementoProyecto } from '@/shared/model/elemento-proyecto.model';

import ElementoService from '@/entities/elemento/elemento.service';
import ElementoProyectoService from './elemento-proyecto.service';
import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ElementoProyectoUpdate',
  components: {
    Editor,
  },

  setup() {
    const { t: t$ } = useI18n();
    const router = useRouter();
    const route = useRoute();

    const dataUtils = useDataUtils();

    const elementoProyectoService = new ElementoProyectoService();
    const elementoService = new ElementoService();

    const alertService = inject('alertService', () => useAlertService(), true);

    const elementoProyecto: Ref<IElementoProyecto> = ref(new ElementoProyecto());
    const elementos: Ref<IElemento[]> = ref([]);

    const isLoading = ref(true);
    const isSaving = ref(false);

    const form = reactive({
      dato: '',
      descripcion: '',
      elemento: null,
    });
    const v$ = useVuelidate({
      form: {
        dato: {},
        descripcion: {},
        elemento: {},
      },
    }, { form });

    onMounted(async() => {
      if (route.params?.elementoProyectoId) {
        await getElementoProyecto(route.params.elementoProyectoId);
      }

      await getElementos();

      isLoading.value = false;
    });

    const previousState = () => router.go(-1);

    const getElementoProyecto = async (elementoProyectoId: any) => {
      try {
        const res = await elementoProyectoService.find(elementoProyectoId);
        elementoProyecto.value = res;

        form.dato = elementoProyecto.value.dato ?? '';
        form.descripcion = elementoProyecto.value.descripcion ?? '';
        form.elemento = elementoProyecto.value.elemento?.id ?? null as any;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };
    const getElementos = async () => {
      try {
        const res = await elementoService.retrieve();
        elementos.value = res.data;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const imageUpload = (blobInfo: any, progress: (percent: number) => void): Promise<string> => {
      return new Promise(async (resolve, reject) => {
        const formData = new FormData();
        formData.append('file', blobInfo.blob(), blobInfo.filename());

        try {
          const response = await axios.post('api/elemento-proyectos/upload/image', formData, {
            onUploadProgress: event => {
              if (event.total) {
                progress((event.loaded / event.total) * 100); // Actualiza el progreso
              }
            },
          });

          const imageUrl = response.data?.src;

          if (imageUrl) {
            resolve(imageUrl); // Resuelve la promesa con la URL de la imagen
          } else {
            reject('No se pudo obtener la URL de la imagen.');
          }
        } catch (error: any) {
          reject('Error al cargar la imagen: ' + (error.response?.data?.message || error.message));
        }
      });
    };

    const save = async () => {
      const formValid = await v$.value.$validate();
      if (!formValid) return;

      isSaving.value = true;

      const dataSend: any = {
        dato: form.dato,
        descripcion: form.descripcion,
        elemento: elementos.value.find(item => item.id === 1101),
      };

      if (elementoProyecto.value.id) {
        dataSend.id = elementoProyecto.value.id;

        await elementoProyectoService.update(dataSend).then(param => {
          isSaving.value = false;
          alertService.showInfo(t$('ciecytApp.elementoProyecto.updated', { param: param.id }));

          previousState();
        }).catch(error => {
          isSaving.value = false;
          alertService.showHttpError(error.response);
        });
      } else {
        await elementoProyectoService.create(dataSend).then(param => {
          isSaving.value = false;
          alertService.showSuccess(t$('ciecytApp.elementoProyecto.created', { param: param.id }).toString());

          previousState();
        }).catch(error => {
          isSaving.value = false;
          alertService.showHttpError(error.response);
        });
      }
    }

    return {
      ...dataUtils,
      isLoading,
      form,
      v$,
      t$,

      elementoProyecto,
      elementos,
      isSaving,

      previousState,
      imageUpload,
      save,
    };
  }
});
