<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="proyecto">
        <h2 class="jh-entity-heading" data-cy="proyectoDetailsHeading">
          <span v-text="t$('ciecytApp.proyecto.detail.title')"></span> {{ proyecto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ciecytApp.proyecto.nombre')"></span>
          </dt>
          <dd>
            <span>{{ proyecto.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('ciecytApp.proyecto.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ proyecto.descripcion }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="proyecto.id" :to="{ name: 'ProyectoEdit', params: { proyectoId: proyecto.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./proyecto-details.component.ts"></script>
