<template>
  <div class="category">
    <van-nav-bar v-show="searchValue != undefined" left-arrow @click-left=$router.back(-1)
      :title="searchType.type + '包含 ' + searchValue + ' 的如下:' " />
    <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad()">
      <van-card v-for="book, index in books" :key="index" :price="book.price" :desc="book.author" :title="book.name"
        :thumb="'/' + book.img" @click="toDetail(book.id)">
        <template #tags>
          <van-tag plain size="large" type="primary">{{ book.publisher }}</van-tag>
          <br>
          <van-tag type="danger">{{ book.etc.cateName }}</van-tag>
          <van-tag @click="toShopCart(book.id)">
            <van-icon name="cart" />
            <!-- <van-icon name="cart-o" /> -->
          </van-tag>
          <van-tag @click="toFavor(book.id)">
             
            <van-icon name="star" v-if="book.etc.isFavor == 1" />
            <van-icon v-else name="star-o" />
          </van-tag>
        </template>
      </van-card>
    </van-list>
  </div>
</template>

<script>
import api from '@/api'
import { Toast } from 'vant';
import { Dialog } from 'vant';

export default {
  props: ['cate'],
  name: 'category',
  data: function () {
    return {
      books: [],
      // category: [],
      // name: [],
      loading: false,
      finished: false,
      refreshing: false,
      pageNum: 1,
      pageSize: 5,
      total: 0,
      searchValue: this.$route.query.searchValue,
      searchType: { type: this.$route.query.type, value: this.$route.query.typeValue },
    }
  },
  watch: {
    cate: function () {
      // this.category = this.cate;
      // console.log(this.category);
      // this.categories.forEach(item => {
      //   if (item.id != 0)
      //     this.name.push(item.name);
      // });
      this.pageNum = 1;
      this.books = [];
      this.finished = false,
      this.onLoad();
      // console.log(this.category.id);

    }
  },
  methods: {
    onLoad() {
      // 异步更新数据

      let self = this;
      if (this.searchValue != undefined) {
        // console.log(this.searchType);
        api.searchBookByKey("/book/searchBookByKey", this.pageNum++, this.pageSize, parseInt(this.searchType.value) + 1, this.searchValue)
          .then(function (res) {
            // console.log(res.data);
            self.books.push(...res.data.records);
            // console.log(res.data);

            // 加载状态结束
            self.loading = false;

            // 数据全部加载完成
            self.total = res.data.total
            if (self.books.length >= self.total) {
              self.finished = true;
            }
          })
      }
      else {
        // console.log(self.category.id);
        api.getBookByCate("/book/getBookByCate", this.pageNum++, this.pageSize, parseInt(this.cate))
          .then(function (res) {
            self.books.push(...res.data.records);
            // console.log(res.data);
            // 加载状态结束
            self.loading = false;
            // 数据全部加载完成
            self.total = res.data.total
            if (self.books.length >= self.total) {
              self.finished = true;
            }
          })
      }
      // }, 1000);
    },
    toDetail(id) {
      this.$router.push({ path: '/detail', query: { id: id } });
    },
    toShopCart(id) {
      event.stopPropagation();
      // this.$store.commit('addCart', id);
      let self = this
      const token = localStorage.getItem('Authorization');
      if (!token) {
        Dialog.confirm({
          message: '尚未登录，需要登录',
          confirmButtonText: '前往登录',
          cancelButtonText: '留在当前页'
        })
          .then(() => {
            // on confirm
            self.$router.replace('/login')
            Toast("请登录")
          })
          .catch(() => {
            // on cancel
          });
      }
      else {
        api.add("/cart/addById", id)
          .then(res => {
            console.log(res.data);
            Toast("添加成功");
            self.$store.dispatch('setCartNum');
          })
      }
    },
    toFavor(id) {
      event.stopPropagation();
      let self = this;
      const token = localStorage.getItem('Authorization');
      if (!token) {
        Dialog.confirm({
          message: '尚未登录，需要登录',
          confirmButtonText: '前往登录',
          cancelButtonText: '留在当前页'
        })
          .then(() => {
            // on confirm
            self.$router.replace('/login')
            Toast("请登录")
          })
          .catch(() => {
            // on cancel
          });
        // self.$router.replace('/login');
        // Toast("请登录");
      }
      else {
        api.add("/favor/addById", id)
          .then(res => {
            // console.log(res.data);
            if (res.statusCode == 500) {//已收藏，取消收藏
              api.deleteByIds("/favor/deleteByIds", id)
                .then(res2 => {
                  // console.log(res2.data);
                  Toast("取消收藏成功");
                  self.$store.dispatch('setFavorNum');
                  self.books[self.books.findIndex(i => i.id == id)].etc.isFavor = 0
                })
            }
            else if (res.statusCode == 200) {
              Toast("收藏成功");
              self.$store.dispatch('setFavorNum');
              self.books[self.books.findIndex(i => i.id == id)].etc.isFavor = 1
            }
          })
      }
    }
  }
}
</script>

<style scoped>
.category {
  text-align: left;
}

.van-card {
  height: 120px;
}

.van-tag--default {
  margin-right: 25px;
  top: 25px;
  float: right;
  height: 23px;
  background-color: rgb(238, 10, 36);
}
</style>
