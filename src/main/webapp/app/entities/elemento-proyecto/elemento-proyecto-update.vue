<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form v-if="!isLoading" name="editForm" @submit.prevent="save()">
        <h2
          id="ciecytApp.elementoProyecto.home.createOrEditLabel"
          data-cy="ElementoProyectoCreateUpdateHeading"
          v-text="t$('ciecytApp.elementoProyecto.home.createOrEditLabel')"
        ></h2>

        <div>
          <div v-if="elementoProyecto.id" class="form-group">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="elementoProyecto.id" readonly />
          </div>

          <div class="form-group">
            <label class="form-control-label" for="elemento-proyecto-dato">
              {{ t$('ciecytApp.elementoProyecto.dato') }}
            </label>

            <Editor
              id="elemento-proyecto-dato"
              api-key="ugf2fm74q3nvz5qz4lt7rz32dviqga8ryv7luxfv18knay2m"
              v-model="v$.form.dato.$model"
              :init="{
                height: 500,
                menubar: false,
                plugins: 'image code',
                toolbar: 'undo redo | styleselect | bold italic | image | code',
                images_upload_handler: imageUpload,
                setup: (editor: any) => {
                  editor.on('init', () => {
                    editor.setContent(v$.form.dato.$model);
                  });
                  editor.on('Change', () => {
                    v$.form.dato.$model = editor.getContent();
                  });
                },
              }"
              :class="{ valid: !v$.form.dato.$invalid, invalid: v$.form.dato.$invalid }"
            />
          </div>

          <div class="form-group">
            <label class="form-control-label" for="elemento-proyecto-descripcion">
              {{ t$('ciecytApp.elementoProyecto.descripcion') }}
            </label>

            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="elemento-proyecto-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.form.descripcion.$invalid, invalid: v$.form.descripcion.$invalid }"
              v-model="v$.form.descripcion.$model"
            />
          </div>

          <div class="form-group">
            <label class="form-control-label" for="elemento-proyecto-elemento">
              {{ t$('ciecytApp.elementoProyecto.elemento') }}
            </label>

            <select
              id="elemento-proyecto-elemento"
              class="form-control"
              data-cy="elemento"
              name="elemento"
              v-model="v$.form.elemento.$model"
            >
              <option v-bind:value="null"></option>

              <template v-for="elementoOption in elementos" :key="elementoOption.id">
                <option
                  :value="elementoOption.id"
                  :selected="elementoOption.id === elementoProyecto.elemento?.id"
                >
                  {{ elementoOption.elemento }}
                </option>
              </template>
            </select>
          </div>
        </div>

        <div>
          <button
            type="button"
            id="cancel-save"
            data-cy="entityCreateCancelButton"
            class="btn btn-secondary me-2"
            @click="previousState()"
          >
            <font-awesome-icon icon="ban"></font-awesome-icon>
            &nbsp;
            <span v-text="t$('entity.action.cancel')"></span>
          </button>

          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>
            &nbsp;
            <span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./elemento-proyecto-update.component.ts"></script>
