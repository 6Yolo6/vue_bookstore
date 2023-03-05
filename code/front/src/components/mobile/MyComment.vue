<template>
    <div class="myComment">
        <van-nav-bar left-arrow @click-left=$router.go(-1) :title="'我的评价'+commentNum">
            <van-icon style="margin-right: 10px;" name="search" slot="right" />
            <span style="margin-right: 10px;" slot="right">管理</span>
            </van-nav-bar>
            <van-notice-bar v-if="this.$store.getters.getCommentNum == ''">您的评价空空如也...</van-notice-bar>
            <div v-for="book, index in books" :key="index" class="book">
                <div v-if="book.appendContent != null" style="margin-bottom: 15px">
                    <div>{{ book.appendTime }}</div>
                    <span style="color:red">用户{{ time(book) }}天后追评  </span> 
                    <span class="content">{{ book.appendContent }}</span>
                    <div style="display:flex" @click="toDetail(book.bookId)">
                        <van-image width="50" height="50" :src="book.etc.book.img" />
                        <div class="bookName">{{ book.etc.book.name }}</div>
                    </div>
                </div>
                <div>
                    <div>{{ book.createTime }}</div>
                    <div><van-rate v-model="book.rate" readonly /></div>
                    <div class="content">{{ book.content }}</div>
                    <div style="display:flex" @click="toDetail(book.bookId)">
                        <van-image width="50" height="50" :src="book.etc.book.img" />
                        <div class="bookName">{{ book.etc.book.name }}</div>
                    </div>
                </div>
            </div>
    </div>
</template>

<script>
import api from '@/api';

export default {
    name: 'myComment',
    data: function () {
        return {
            books: [],
            value: 0,

        }
    },
    methods: {
        toDetail(id) {
            this.$router.push({ path: '/detail', query: { id: id } });
        },
        time(book) {
            return parseInt((new Date(book.appendTime).getTime() - new Date(book.createTime).getTime()) / 1000 / 60 / 60 / 24)
        },
        getComment() {
            let self = this;
            api.getByUserId("/comment/getByUserId")
                .then(res => {
                    console.log(res);
                    if (res.statusCode == 200) {
                        self.books = res.data
                        self.$store.commit('setCommentNum', res.data.length);
                    }
                })
                .catch(error => {
                    console.log(error);
                })
        }
    },
    computed: {
        commentNum: function () {
            let n = this.$store.getters.getCommentNum;
            return n > 0 ? "(" + n + ")" : '';
        }
    },
    created: function () {
        this.getComment();
    }
}
</script>

<style scoped>
.myComment {
    padding: 15px;
    text-align: left;
}

.book div {
    margin-bottom: 15px;
}
/* .book div:first-child {
    margin-top: 15px;
} */
.book {
    border-radius: 15px;
    background-color: white;
}
.bookName {
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 250px;
    white-space: nowrap;
    line-height: 50px;
}
.content {
    color: rgb(34, 196, 201)
}
</style>