<template>
  <div>
    <h2 id="page-heading" data-cy="IntegrantesProyectoHeading">
      <span v-text="t$('ciecytApp.integrantesProyecto.home.title')" id="integrantes-proyecto-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('ciecytApp.integrantesProyecto.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'IntegrantesProyectoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-integrantes-proyecto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('ciecytApp.integrantesProyecto.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && integrantesProyectos && integrantesProyectos.length === 0">
      <span v-text="t$('ciecytApp.integrantesProyecto.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="integrantesProyectos && integrantesProyectos.length > 0">
      <table class="table table-striped" aria-describedby="integrantesProyectos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="t$('ciecytApp.integrantesProyecto.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('descripcion')">
              <span v-text="t$('ciecytApp.integrantesProyecto.descripcion')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('proyecto.id')">
              <span v-text="t$('ciecytApp.integrantesProyecto.proyecto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'proyecto.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="integrantesProyecto in integrantesProyectos" :key="integrantesProyecto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IntegrantesProyectoView', params: { integrantesProyectoId: integrantesProyecto.id } }">{{
                integrantesProyecto.id
              }}</router-link>
            </td>
            <td>{{ integrantesProyecto.nombre }}</td>
            <td>{{ integrantesProyecto.descripcion }}</td>
            <td>
              <div v-if="integrantesProyecto.proyecto">
                <router-link :to="{ name: 'ProyectoView', params: { proyectoId: integrantesProyecto.proyecto.id } }">{{
                  integrantesProyecto.proyecto.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'IntegrantesProyectoView', params: { integrantesProyectoId: integrantesProyecto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'IntegrantesProyectoEdit', params: { integrantesProyectoId: integrantesProyecto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(integrantesProyecto)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="ciecytApp.integrantesProyecto.delete.question"
          data-cy="integrantesProyectoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-integrantesProyecto-heading" v-text="t$('ciecytApp.integrantesProyecto.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-integrantesProyecto"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeIntegrantesProyecto()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="integrantesProyectos && integrantesProyectos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./integrantes-proyecto.component.ts"></script>
