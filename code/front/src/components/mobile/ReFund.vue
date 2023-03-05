<template>
  <div class="refund">
        <van-nav-bar left-text="退款/售后" left-arrow @click-left=$router.go(-1)>
            <template #right>
                <van-icon size="25" name="ellipsis" />
            </template>
        </van-nav-bar>
        <div class="content">
            <van-card v-for="book, index in books" :key="index" :price="'退款:￥'+book.price" :desc="'编号'+book.no"
                :title="'创建时间'+book.createTime">
                <template #footer>
                    <van-button type="primary" @click="deleteOrder(book)">删除订单</van-button>
                </template>
            </van-card>
        </div>
  </div>
</template>

<script>
import api from '@/api';

export default {
    name: 'reFund',
    data: function () {
        return {
            books: [],

        }
    },
    created: function () {
        if (this.$route.query.books == '') {
            let self = this
            api.getByStatus("/order/getByStatus", this.$route.query.status)
                .then(res => {
                    console.log(res)
                    self.books = res.data
                })
        }
        else
            this.books = this.$route.query.books;
        // console.log(this.$route.query.books, "books");
        // console.log(this.$route.query.books == '');
        // console.log(this.$route.query.status, "status");
    },
    methods: {
        deleteOrder(books) {
            console.log("删除", books);
        }
    }
}
</script>

<style scoped>
.content {
    text-align: left;
}

.van_card {
    font-size: 16px;
}
</style>