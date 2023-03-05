<template>
  <div class="home">
    <van-search v-model="value" background="white" placeholder="商品搜索" @search="search(option1[type], value)">
      <template slot="left">
        <van-dropdown-menu style="display: flex;">
          <van-dropdown-item v-model="type" class="popup" :options="option1" />
        </van-dropdown-menu>
      </template>
    </van-search>
    <nav v-show="searchFlag != true">
      <van-swipe :autoplay="3000">
        <van-swipe-item v-for="(image, index) in images" :key="index">
          <img v-lazy="image" />
        </van-swipe-item>
      </van-swipe>

      <van-grid :column-num="3" :gutter="10">
        <van-grid-item :class="{ active: i === index }" v-for="item, index in categories" :key="index" :text="item.name"
          icon="orders-o" @click="onChange(index, item)">
        </van-grid-item>
      </van-grid>
      
    </nav>
    <Category :cate="category"></Category>
  </div>
</template>
<script>

import Category from '@/components/mobile/Category.vue'
import api from '@/api'

export default {
  name: 'home',
  components: {
    Category
  },
  data: function () {
    return {
      categories: [],
      item: [],
      i: 0,
      category: '',
      value: '',
      searchFlag: false,
      images: [
        require('/public/slider/1.jpg'),
        require('/public/slider/2.jpg'),
        require('/public/slider/3.jpg'),
        require('/public/slider/4.jpg'),
        require('/public/slider/5.jpg'),
      ],
      type: 0,
      option1: [
        { text: '书名', value: 0 },
        { text: '作者', value: 1 },
        { text: '出版社', value: 2 },
      ],
    }
  },
  methods: {
    onChange(i, item) {
      this.i = i;
      // this.category = { id: item.id, name: item.name };
      this.category = item.id
      // console.log(this.category);
    },
    search(type, value) {
      this.searchFlag = true;
      // console.log(type);
      this.$router.push({ path: '/category', query: { type: type.text, typeValue: type.value, searchValue: value } });
    },
  },
  mounted: function () {
    this.searchFlag = false;
    let self = this;
    api.getAllCate("/category/getAllCate")
      .then(res => {
        self.categories = res.data;
        self.categories.splice(0, 0, { id: 0, name: "全部" });
        self.category = self.categories[0].id;
        // console.log(self.category);
        // console.log(res.data);
      })
  }
}
</script>

<style scoped>
.home {
  background-color: white;
}

.active {
  color: #1989fa;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

img {
  width: auto;
  height: 180px;
}

.popup {
    left: 10px;
    width: 30%;
    padding: 10px;
}
</style>