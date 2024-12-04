<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="elemento">
        <h2 class="jh-entity-heading" data-cy="elementoDetailsHeading">
          <span v-text="t$('ciecytApp.elemento.detail.title')"></span> {{ elemento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ciecytApp.elemento.elemento')"></span>
          </dt>
          <dd>
            <span>{{ elemento.elemento }}</span>
          </dd>
          <dt>
            <span v-text="t$('ciecytApp.elemento.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ elemento.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('ciecytApp.elemento.proyecto')"></span>
          </dt>
          <dd>
            <div v-if="elemento.proyecto">
              <router-link :to="{ name: 'ProyectoView', params: { proyectoId: elemento.proyecto.id } }">{{
                elemento.proyecto.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="elemento.id" :to="{ name: 'ElementoEdit', params: { elementoId: elemento.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./elemento-details.component.ts"></script>
