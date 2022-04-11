<template>
  <div class="main-container">
    <div class="choose-difficult-btns">
      <button @click="setLevel('easy')">Easy</button>
      <button @click="setLevel('medium')">Medium</button>
      <button @click="setLevel('expert')">Expert</button>
    </div>

    <div class="headline" v-if="!level">
      <h2>Please chose a level on your sudukoboard</h2>
    </div>

    <div v-if="level">
      <Suduko :chosenLevel="lev" />
    </div>
  </div>
</template>

<script>
import Suduko from "../components/Suduko.vue";
export default {
  data() {
    return {
      level: "",
    }
  },
  computed: {
    lev() {
      return this.level;
    }
  },
  components: {
    Suduko,
  },
  methods: {
    setLevel(level) {
      if (this.level === level) {
        this.level = "reload"
        return;
      }
      this.level = level;

    }
  }
}
</script>

<style scoped>
.headline {
  display: flex;
  justify-content: center;
}
.main-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.choose-difficult-btns {
  margin: 20px;
  display: flex;
  justify-content: space-around;
  width: 30%;
  align-self: center;
}
</style>