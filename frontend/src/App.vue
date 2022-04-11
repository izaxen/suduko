<script setup>
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
  <header>
    <h2>Suduko Test</h2>
  </header>

  <RouterView />
</template>

<style scoped>
header {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #005269;
  text-align: center;
}

h2 {
  margin-right: 30px;
}
</style>
