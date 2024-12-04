<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate v-on:submit.prevent="save()">
        <h2
          id="ciecytApp.integrantesProyecto.home.createOrEditLabel"
          data-cy="IntegrantesProyectoCreateUpdateHeading"
          v-text="t$('ciecytApp.integrantesProyecto.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="integrantesProyecto.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="integrantesProyecto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('ciecytApp.integrantesProyecto.nombre')" for="integrantes-proyecto-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="integrantes-proyecto-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('ciecytApp.integrantesProyecto.descripcion')"
              for="integrantes-proyecto-descripcion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="integrantes-proyecto-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('ciecytApp.integrantesProyecto.proyecto')"
              for="integrantes-proyecto-proyecto"
            ></label>
            <select
              class="form-control"
              id="integrantes-proyecto-proyecto"
              data-cy="proyecto"
              name="proyecto"
              v-model="integrantesProyecto.proyecto"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  integrantesProyecto.proyecto && proyectoOption.id === integrantesProyecto.proyecto.id
                    ? integrantesProyecto.proyecto
                    : proyectoOption
                "
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
<script lang="ts" src="./integrantes-proyecto-update.component.ts"></script>
