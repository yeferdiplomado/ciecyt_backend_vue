<template>
  <div>
    <h2 id="page-heading" data-cy="ElementoProyectoHeading">
      <span v-text="t$('ciecytApp.elementoProyecto.home.title')" id="elemento-proyecto-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('ciecytApp.elementoProyecto.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ElementoProyectoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-elemento-proyecto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('ciecytApp.elementoProyecto.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && elementoProyectos && elementoProyectos.length === 0">
      <span v-text="t$('ciecytApp.elementoProyecto.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="elementoProyectos && elementoProyectos.length > 0">
      <table class="table table-striped" aria-describedby="elementoProyectos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dato')">
              <span v-text="t$('ciecytApp.elementoProyecto.dato')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dato'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('descripcion')">
              <span v-text="t$('ciecytApp.elementoProyecto.descripcion')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('elemento.id')">
              <span v-text="t$('ciecytApp.elementoProyecto.elemento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'elemento.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="elementoProyecto in elementoProyectos" :key="elementoProyecto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ElementoProyectoView', params: { elementoProyectoId: elementoProyecto.id } }">{{
                elementoProyecto.id
              }}</router-link>
            </td>
            <td v-html="sanitizeDato(elementoProyecto.dato ?? '')"></td>
            <td>{{ elementoProyecto.descripcion }}</td>
            <td>
              <div v-if="elementoProyecto.elemento">
                <router-link :to="{ name: 'ElementoView', params: { elementoId: elementoProyecto.elemento.id } }">{{
                  elementoProyecto.elemento.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ElementoProyectoView', params: { elementoProyectoId: elementoProyecto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ElementoProyectoEdit', params: { elementoProyectoId: elementoProyecto.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(elementoProyecto)"
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
          id="ciecytApp.elementoProyecto.delete.question"
          data-cy="elementoProyectoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-elementoProyecto-heading" v-text="t$('ciecytApp.elementoProyecto.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-elementoProyecto"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeElementoProyecto()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="elementoProyectos && elementoProyectos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./elemento-proyecto.component.ts"></script>
