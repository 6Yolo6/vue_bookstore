<template>
  <div class="bookComment">
    <van-nav-bar left-arrow @click-left=$router.back(-1) :title="'书籍评价'+bookCommentNum">
            <van-icon style="margin-right: 10px;" name="search" slot="right" />
            <span style="margin-right: 10px;" slot="right">管理</span>
        </van-nav-bar>
        <div v-for="comment, index in comments" :key="index" class="book">
            <div v-if="comment.appendContent != null" style="margin-bottom: 15px">
                <div>{{ comment.appendTime }}</div>
                <span style="color:red">用户{{ time(comment) }}天后追评 </span>
                <span>{{ comment.appendContent }}</span>
                <div style="display:flex"><van-image round class="img" src="user.jpg" />
                    <div style="margin-left: 20px;margin-top: 10px;">{{comment.etc.nickname}}</div>
                </div>
            </div>
            <div>
                <div style="font-size:14px">{{comment.createTime}}</div>
                <div style="display:flex"><van-image round class="img" src="user.jpg" />
                    <div style="margin-left: 20px;margin-top: 10px;">{{comment.etc.nickname}}</div>
                </div>
                <div><van-rate v-model="comment.rate" readonly /></div>
                <div>{{ comment.content }}</div>
            </div>
        </div>
  </div>
</template>

<script>
import api from '@/api';
export default {
    name: 'bookComment',
    data: function () {
        return {
            comments: [],

        }
    },
    methods: {
        time(book) {
            return parseInt((new Date(book.appendTime).getTime() - new Date(book.createTime).getTime()) / 1000 / 60 / 60 / 24)
        },
        getComment(bookId) {
            let self = this;
            api.getCommentByBookId("/comment/getByBookId", bookId)
                .then(res => {
                    console.log(res)
                    
                    if (res.statusCode == 200) {
                        self.comments = res.data
                    self.$store.commit('setbookCommentNum', res.data.length);
                    }
                })
        }
    },
    computed: {
        bookCommentNum: function () {
            let n = this.$store.getters.getbookCommentNum;
            return n > 0 ? "(" + n + ")" : '0';
        }
    },
    created: function () {
        this.getComment(this.$route.query.bookId);
    }
}
</script>

<style scoped>
.bookComment {
    text-align: left;
    padding: 15px;
    
}
.book div {
    margin-bottom: 15px;
}
.book {
    border-radius: 15px;
    background-color: white;
}
.img {
    width:40px;
    height:40px;
}
</style>