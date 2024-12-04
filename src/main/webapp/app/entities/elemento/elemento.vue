<template>
  <div>
    <h2 id="page-heading" data-cy="ElementoHeading">
      <span v-text="t$('ciecytApp.elemento.home.title')" id="elemento-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('ciecytApp.elemento.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ElementoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-elemento"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('ciecytApp.elemento.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && elementos && elementos.length === 0">
      <span v-text="t$('ciecytApp.elemento.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="elementos && elementos.length > 0">
      <table class="table table-striped" aria-describedby="elementos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('elemento')">
              <span v-text="t$('ciecytApp.elemento.elemento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'elemento'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('descripcion')">
              <span v-text="t$('ciecytApp.elemento.descripcion')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('proyecto.id')">
              <span v-text="t$('ciecytApp.elemento.proyecto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'proyecto.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="elemento in elementos" :key="elemento.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ElementoView', params: { elementoId: elemento.id } }">{{ elemento.id }}</router-link>
            </td>
            <td>{{ elemento.elemento }}</td>
            <td>{{ elemento.descripcion }}</td>
            <td>
              <div v-if="elemento.proyecto">
                <router-link :to="{ name: 'ProyectoView', params: { proyectoId: elemento.proyecto.id } }">{{
                  elemento.proyecto.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ElementoView', params: { elementoId: elemento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ElementoEdit', params: { elementoId: elemento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(elemento)"
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
        <span id="ciecytApp.elemento.delete.question" data-cy="elementoDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-elemento-heading" v-text="t$('ciecytApp.elemento.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-elemento"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeElemento()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="elementos && elementos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./elemento.component.ts"></script>
