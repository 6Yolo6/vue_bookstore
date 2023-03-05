<template>
  <div class="comment">
    <van-nav-bar :left-text="book.status == -1 ? '发表追评' : '发表评价'" left-arrow @click-left="back">
      <template #right>
        <van-button round type="info" size="big" @click="publish()">{{ book.status == -1 ? "发布追评" : "发布评价" }}</van-button>
      </template>
    </van-nav-bar>
    <div class="content">
      <div>
        <van-image width="50" height="50" :src="book.img" />
        <div style="font-size:14px;margin-bottom: 10px;">{{ book.name }}</div>
        <div v-if="book.status != -1">
          <span style="margin-right: 20px;">评分</span>
        <van-rate v-model="value" @change="onChange" />
        </div>
        <span style="margin-left: 25px;">{{ rate[value-1] }}</span>
      </div>
      <div>
        <van-field style="border-radius: 16px;" v-model="message" rows="4" autosize :label="book.status == -1 ? '追评' : '评价'" type="textarea" maxlength="60" placeholder="请输入内容"
          show-word-limit />
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api';
import { Toast } from 'vant';
export default {
  name: 'comment',
    data: function () {
        return {
          book: this.$route.query.book,
          rate: '',
          value: 0,
          message: '',
          rate: ['非常差','差','一般','好','非常好'],

        }
    },
    methods: {
      back() {
        this.$router.push({ path: '/order', query: { status: this.book.status } });
      },
      onChange(value) {
        // let rate = ['非常差','差','一般','好','非常好'];
        // this.rate = rate[value-1];
        this.value = value;
      },
      publish() {
        // console.log(this.message);

        let self = this;
        if (this.book.status == 4) {//评价
          api.addComment("/comment/add", this.book.bookId, this.value, this.message)
            .then(res => {
              console.log(res);
              api.updateOrderDetail("/orderDetail/update", self.book.orderId, self.book.bookId, self.book.status)
                .then(res2 => {
                  console.log(res2);
                  Toast("评价成功");
                  self.$router.push({ path: '/myComment', query: { book: this.book } });
                })
            })
        }
        else if (this.book.status == -1) {//追评
          api.appendComment("/comment/append", this.book.bookId, this.message)
            .then(res => {
              console.log(res);
              api.updateOrderDetail("/orderDetail/update", self.book.orderId, self.book.bookId, self.book.status)
                .then(res2 => {
                  console.log(res2);
                  Toast("追评成功");
                  self.$router.push({ path: '/myComment', query: { book: this.book } });
                })
            })
        }
      }
    }
}
</script>

<style scoped>
.comment {
  padding: 10px;
}
.content {
  padding: 10px;
}
.van-nav-bar .van-icon {
    color: #000000;
}
.van-nav-bar__text {
    color: #000000;
}
.van-button--info {
    background-color: #fa8119;
    border: 1px solid #fa8119;
}
.van-button {
    height: 36px;
    width: 70px;
}
.van-rate {
  margin-bottom: 20px;
}
</style>