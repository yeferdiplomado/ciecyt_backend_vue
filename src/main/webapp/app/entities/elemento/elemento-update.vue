<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate v-on:submit.prevent="save()">
        <h2
          id="ciecytApp.elemento.home.createOrEditLabel"
          data-cy="ElementoCreateUpdateHeading"
          v-text="t$('ciecytApp.elemento.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="elemento.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="elemento.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ciecytApp.elemento.elemento')" for="elemento-elemento"></label>
            <input
              type="text"
              class="form-control"
              name="elemento"
              id="elemento-elemento"
              data-cy="elemento"
              :class="{ valid: !v$.elemento.$invalid, invalid: v$.elemento.$invalid }"
              v-model="v$.elemento.$model"
              required
            />
            <div v-if="v$.elemento.$anyDirty && v$.elemento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.elemento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ciecytApp.elemento.descripcion')" for="elemento-descripcion"></label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="elemento-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ciecytApp.elemento.proyecto')" for="elemento-proyecto"></label>
            <select class="form-control" id="elemento-proyecto" data-cy="proyecto" name="proyecto" v-model="elemento.proyecto">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="elemento.proyecto && proyectoOption.id === elemento.proyecto.id ? elemento.proyecto : proyectoOption"
                v-for="proyectoOption in proyectos"
                :key="proyectoOption.id"
              >
                {{ proyectoOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./elemento-update.component.ts"></script>
