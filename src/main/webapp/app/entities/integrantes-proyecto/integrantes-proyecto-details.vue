<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="integrantesProyecto">
        <h2 class="jh-entity-heading" data-cy="integrantesProyectoDetailsHeading">
          <span v-text="t$('ciecytApp.integrantesProyecto.detail.title')"></span> {{ integrantesProyecto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('ciecytApp.integrantesProyecto.nombre')"></span>
          </dt>
          <dd>
            <span>{{ integrantesProyecto.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('ciecytApp.integrantesProyecto.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ integrantesProyecto.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('ciecytApp.integrantesProyecto.proyecto')"></span>
          </dt>
          <dd>
            <div v-if="integrantesProyecto.proyecto">
              <router-link :to="{ name: 'ProyectoView', params: { proyectoId: integrantesProyecto.proyecto.id } }">{{
                integrantesProyecto.proyecto.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="integrantesProyecto.id"
          :to="{ name: 'IntegrantesProyectoEdit', params: { integrantesProyectoId: integrantesProyecto.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./integrantes-proyecto-details.component.ts"></script>
