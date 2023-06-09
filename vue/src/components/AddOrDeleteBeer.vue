<template>
  <div id="allBeer">
    <!-- if user is admin or brewer, the add/delete form will show. if they are user, then it wont show -->
    <div v-if="isAdminOrBrewer">

      <h2>Add or Delete Beer</h2>
      <!-- Add Beer -->
      <form @submit.prevent="addBeer">
        <h3>Add Beer</h3>
          <label for="name">Name:</label>
          <input type="text" id="name" v-model="newBeer.name" required>
          <br><br>

          <label for="description">Description:</label><br>
          <textarea id="description" v-model="newBeer.description"></textarea>
          <br><br>

          <label for="abv">ABV:</label>
          <input type="number" id="abv" v-model="newBeer.abv" step="0.01" required>
          <br><br>

          <label for="beerType">Beer Type:</label>
          <input type="text" id="beerType" v-model="newBeer.beerType" required>
          <br><br>

          <!-- loop through filteredBreweries to select brewery based on user logged in -->
          <label for="breweryId">Brewery:</label>&nbsp;
          <select id="breweryId" v-model="newBeer.breweryId">
            <option v-for="brewery in filteredBreweries" :value="brewery.breweryId" :key="brewery.breweryId">{{ brewery.name }}</option>
          </select>
          <br><br>
          <button type="submit" id="addBeer">Add Beer</button>
      </form>

      <!-- Delete Beer -->
      <form @submit.prevent="deleteBeer">
          <h3>Delete Beer</h3>
          <label for="breweryToDelete">Brewery:</label>&nbsp;
          <select id="breweryToDelete" v-model="breweryToDelete">
              <option v-for="brewery in filteredBreweries" :value="brewery.breweryId" :key="brewery.breweryId">{{ brewery.name }}</option>
          </select>
          <br><br>
          <label for="beerId">Beer:</label>&nbsp;
          <select id="beerId" v-model="beerToDelete">
              <option v-for="beer in filteredBeers" :value="beer.beerId" :key="beer.beerId">{{ beer.name }}</option>
          </select>
          <br><br>
          <button type="submit" id="deleteBeer">Delete Beer</button>
      </form>
    </div>
  </div>
</template>

<script>
import BeerService from '../services/BeerService';
import { mapGetters } from 'vuex';

export default {
  name: 'AddOrDeleteBeer',
  //passing data properties from brewer views page to this component.
  props: {
  userBreweries: {
    type: Array,
    required: true
  }
},
computed: {
  // import user object from store, set up getter for this
  ...mapGetters(['user']),
  isAdminOrBrewer() {
    // check if user has role of admin or brewer
    console.log("User object:", this.user);
    return this.user.role === "ROLE_ADMIN" || this.user.role === "ROLE_BREWER";
  },
  // filter breweries array to only include the breweries that belong to the logged-in user
  filteredBreweries() {
    if (this.userBreweries) {
      return this.userBreweries.filter(brewery => brewery.userId === this.user.id);
    } else {
      return [];
    }
  },

  //filter beers from breweries
  filteredBeers() {
    if (this.breweryToDelete) {
      return this.brewersBeers.filter(beer => beer.breweryId === this.breweryToDelete);
    } else {
      return [];
    }
  }
},
  data() {
    return {
      //used for beers being added, initial values
      newBeer: {
        name: '',
        description: '',
        abv: null,
        beerType: '',
        breweryId: null
      },
      beerToDelete: null,
      // beers from the brewer

      brewersBeers:[],
      breweryToDelete: null,
    }
  },
  created(){
    // get beers linked to brewer
    this.fetchBrewersBeers();
  },
  methods: {
    //add beer
    addBeer() {
      BeerService.addBeer(this.newBeer)
        .then(() => {
          alert('Beer added successfully');
          this.newBeer = {
            name: '',
            description: '',
            abv: null,
            beerType: '',
            breweryId: null // set breweryId back to null after adding beer
          };
        })
        .catch((error) => {
          console.log(error);
          alert('Failed to add beer');
        });
    },

    //delete beers
    deleteBeer() {
      BeerService.deleteBeer(this.beerToDelete)
        .then(() => {
          alert('Beer deleted successfully');
          this.beerToDelete = null;
        })
        .catch((error) => {
          console.log(error);
          alert('Failed to delete beer');
        });
    },

    async fetchBrewersBeers(){
      try {
        //get beers linked to the logged in brewer from beer service
      const response = await BeerService.getBeersByUserId(this.user.id);
      //array of beers being returned by the API
      this.brewersBeers = response.data.sort((a, b) => a.name.localeCompare(b.name));
    } catch (error) {
      console.log(error);
      alert("Failed to fetch brewer's beers");
    }
    }
 

  }
}
</script>

<style>
#allBeer{
  margin-left: 50px;
}

#name{
  margin-left: 80px;
}
#description{
  margin-left: 126px;
}
#abv{
  margin-left: 93px;
}
#beerType{
  margin-left: 50px;
}
#addBeer, #deleteBeer{
  border: 1px solid black;
  background: rgba(6,40,81,255);
  background-clip: padding-box;
  color: white;
  padding: 2px 10px;
  border-radius: 5px;
}

</style>
    