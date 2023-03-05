<template>
  <div id="app">
    <div v-if="isAlive">
        <router-view/>
        <nav v-show="this.$route.name != 'detail' && this.$route.name != 'orderDetail'
         && this.$route.name != 'manageBook' && this.$route.name != 'manageOrder'
         && this.$route.name != 'manageUser'&& this.$route.name != 'manageComment'
         && this.$route.name != 'backLogin' && this.$route.name != 'myIndex'
         && this.$route.name != 'myAbout'">
          <van-tabbar route>
            <van-tabbar-item to="/" icon="home-o">主页</van-tabbar-item>
            <van-tabbar-item to="/cart" icon="cart-o" :badge="cartNum">购物车</van-tabbar-item>
            <van-tabbar-item to="/frontUser" icon="manager">我的书城</van-tabbar-item>
          </van-tabbar>
        </nav>
    </div>
  </div>
</template>
<script>
// import api from "@/api"
export default {
  data: function() {
    return { 
      isAlive: true
    }
  },
  provide: function() {
    return {
      reload: this.reload
    }
  },
  methods: {
    reload() {
      this.isAlive = false;
      this.$nextTick(function(){
        this.isAlive = true;
      })
    }
  },
  computed: {
    cartNum: function() {
      // let self = this;
      // if (localStorage.getItem('username') != null) {
      //   // this.$store.commit('setCartNum');
      //   let n = this.$store.getters.getCartNum;
      //   console.log(n, '购物车数量');
      //   return n > 0 ? n : '';
      // }
      // else
      //   return '';
      let n = this.$store.getters.getCartNum;
      console.log(n, '购物车数量');
      return n > 0 ? n : '';
    }
  }
}
</script>
<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: rgb(245,245,245);
}

nav {
  padding: 30px;
  background-color: white;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav button.router-link-exact-active {
  background-color: #42b983;
}

h3{
	background-color: rgb(65,184,131);
	margin:0;
	padding:10px;
	color:white;
  height: 34px;
}
</style>
