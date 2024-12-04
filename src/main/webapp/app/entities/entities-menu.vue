<template>
  <div>
    <b-dropdown-item to="/elemento">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.elemento')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/elemento-proyecto">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.elementoProyecto')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/integrantes-proyecto">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.integrantesProyecto')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/proyecto">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.proyecto')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
