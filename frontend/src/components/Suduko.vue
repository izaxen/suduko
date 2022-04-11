<template>
  <div class="main-container">
    <div class="suduko-board">
      <div class="suduko-rows" v-for="(rows, indexRow) in prefilledBoard" :key="rows">
        <div class="suduko-boxes" v-for="(item, indexCol) in rows" :key="item">
          <div v-if="item !== 0">{{ item }}</div>
          <div v-else>
            <input class="input-box" :id="indexRow + ',' + indexCol" type="text" min="1" max="9" maxlength="1"
              @keyup.stop="checkBox($event.target)" @click="clearColor($event.target)" value />
          </div>
        </div>
      </div>
    </div>
    <div class="btn-under">
      <div class="show-validate-btn" v-if="!autoValidateBtn">
        <button @click="validateNumbers">Validate input</button>
      </div>
      <button @click="solveSuduko">Solve suduko</button>
    </div>
  </div>
</template>

<script>
export default {
  props:
    ["chosenLevel"]
  ,
  data() {
    return {
      autoValidate: false,
      chosenNumbersList: [],
      prefilledBoard: [],
    };
  },
  mounted() {
    this.createNewBoard(this.chosenLevel)
  },

  watch: {
    chosenLevel: function () {
      this.createNewBoard(this.chosenLevel)
    }
  },
  computed: {
    prefillBoard() {
      return this.prefilledBoard;
    },
    autoValidateBtn() {

      return this.autoValidate;
    },
  },
  methods: {
    checkIfSolved() {
      const solved = this.prefilledBoard.map((item) => {
        return item.includes(0)
      });
      if (!solved.includes(true)) {
        alert("Suduko solved")
      }
    },

    async createNewBoard(level) {
      this.prefilledBoard = [];
      this.chosenNumbersList = [];
      const newLevel = new URLSearchParams();
      newLevel.append("level", level);
      this.prefilledBoard = await (
        await fetch(`/rest/getNewBoard?${newLevel}`)
      ).json();
    },

    async checkBox(number) {
      if (number.value >= 1 && number.value <= 9) {
        let splitCordinates = number.id.split(",");
        const valueToValidate = {
          xPosition: Number(splitCordinates[0]),
          yPosition: Number(splitCordinates[1]),
          numberToValidate: Number(number.value),
        };

        this.checkIfPositionExistInList(valueToValidate);
        this.chosenNumbersList.push(valueToValidate);
        if (!this.autoValidate) return;
        await this.validateNumbers();
        return;
      }
      document.getElementById(number.id).value = "";
    },

    checkIfPositionExistInList(valueToValidate) {
      const id = this.chosenNumbersList.findIndex(
        (item) =>
          item.xPosition === valueToValidate.xPosition &&
          item.yPosition === valueToValidate.yPosition
      );
      if (id >= 0) {
        this.chosenNumbersList = this.chosenNumbersList.filter(
          (_num, index) => index !== id
        );
      }
    },

    async validateNumbers() {
      const validatedInput = await (
        await fetch(`/rest/validateInput`, {
          method: "POST",
          body: JSON.stringify(this.chosenNumbersList),
        })
      ).json();
      this.updateBoard(validatedInput);
    },

    updateBoard(validatedInput) {
      validatedInput.forEach((input) => {
        if (input.correctNumber) {
          this.prefilledBoard[input.xPosition][input.yPosition] = Number(
            input.numberToValidate
          );
          this.checkIfSolved();
          return;
        }
        const position = input.xPosition + "," + input.yPosition;
        this.errorColor(position);
      });
      this.chosenNumbersList = [];
    },

    async solveSuduko() {
      this.prefilledBoard = await (await fetch(`/rest/getFullBoard`)).json();
    },

    clearColor(event) {
      document.getElementById(event.id).value = "";
      document.getElementById(event.id).style.backgroundColor = "";
    },
    errorColor(position) {
      document.getElementById(position).style.backgroundColor = "red";
    },
  },
};
</script>

<style scoped>
.main-container {
  width: 90vh;
  display: flex;
  flex-direction: column;
}

.suduko-rows {
  display: grid;
  grid-template-columns: repeat(9, minmax(50px, 50px));
  grid-auto-flow: row;
  text-align: center;
}

.suduko-board {
  align-self: center;
  display: grid;
  grid-template-rows: repeat(9, minmax(50px, 50px));
  border-top: 2px solid black;
  border-left: 2px solid black;
}

.suduko-boxes {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgb(255, 255, 255);
  text-align: center;
  font-size: 150%;
  border: 1px solid rgb(169, 169, 169);
}

.input-box {
  width: 100%;
  border: 0;
  padding: 0;
  margin: 0;
  font-size: 100%;
  font-family: "Times New Roman", Times, serif;
  line-height: 195%;
  text-align: center;
}

.btn-under {
  margin: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.checkbox-autovalidate {
  margin: 20px;
}

.show-validate-btn {
  margin: 0 20px 20px 20px;
}

.suduko-boxes:nth-child(3n + 3) {
  border-right: 2px solid black;
}

.suduko-rows:nth-child(3n + 3) {
  border-bottom: 2px solid rgb(0, 0, 0);
}
</style>
