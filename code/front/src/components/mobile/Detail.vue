<template>
    <div class="detail">
        <van-nav-bar left-arrow @click-left=$router.go(-1) :title="details.name" />
        <van-image :src="'/' + details.img" class="img"></van-image>
        <van-card :price="details.price" :desc="details.author" :title="details.name">
            <template #tags>
                <van-tag plain size="large" type="primary">{{ details.publisher }}</van-tag>
                <br>
                <!-- $route.params.name[details.category-1] -->
                <van-tag size="large" type="danger">{{ details.etc.cateName }}</van-tag>
            </template>
            <template #num>
                <van-tag size="large" class="tagFavor" @click="toFavor($route.query.id)"> 
                    <van-icon name="star" v-if="details.etc.isFavor == 1" />
                    <van-icon name="star-o" v-else />
                    {{ favor }}
                </van-tag>
            </template>
        </van-card>
        <div style="margin-left:20px;color: red;float:left">简介</div>
        <div class="bookDetail" v-html="details.description"></div>
        <van-nav-bar :left-text="'书籍评价'+bookCommentNum" @click-left="toComment()" @click-right="toComment()">
            <template #right>
                查看全部
                <van-icon name="arrow" />
            </template>
        </van-nav-bar>
        <div v-for="comment, index in comments" :key="index" class="book">
            <div style="display:flex"><van-image round class="img2" src="user.jpg"/>
                <div style="margin-left: 20px;margin-top: 10px;">{{comment.etc.nickname}}</div>
            </div>
            <div><van-rate v-model="comment.rate" readonly /></div>
            <div>{{ comment.content }}</div>
            <div style="font-size:14px">{{comment.createTime}}</div>
        </div>
        <van-goods-action>
            <van-goods-action-icon icon="chat-o" text="客服" />
            <van-goods-action-icon icon="cart-o" text="购物车" to="/cart" :badge="cartNum" />
            <van-goods-action-icon icon="star-o" text="收藏" to="/favor" :badge="favorNum" />
            <van-goods-action-button type="danger" text="加入购物车" @click="toShopCart($route.query.id)" />
        </van-goods-action>
    </div>
</template>

<script>
import api from '@/api'
import { Toast } from 'vant';
import { Dialog } from 'vant';

export default {
    name: 'datail',
    data: function () {
        return {
            details: [],
            comments: [],
        }
    },
    methods: {
        toShopCart(id) {
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
            const token = localStorage.getItem('Authorization');
            let self = this;
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
                                    self.details.etc.isFavor = 0
                                })
                        }
                        else if (res.statusCode == 200) {
                            Toast("收藏成功");
                            self.$store.dispatch('setFavorNum');
                            self.details.etc.isFavor = 1
                        }
                    })
            }
        },
        toComment() {
            this.$router.push({path: '/bookComment', query: {bookId: this.details.id}});
        },
    },
    mounted: function () {
        let self = this;
        let id = this.$route.query.id
        api.getBookById("/book/getBookById", id)
            .then(res => {
                self.details = res.data;
                // console.log(self.details.etc.cateName);
                api.getCommentByBookId("/comment/getByBookId", id)
                .then(res2 => {
                    console.log(res2)    
                    self.$store.dispatch('setCartNum')
                    self.$store.dispatch('setFavorNum')                
                    if (res2.statusCode == 200) {
                        self.comments = res2.data.slice(0, 3)
                        self.$store.commit('setbookCommentNum', res2.data.length);
                    }
                    else
                        self.$store.commit('setbookCommentNum', 0);
                })
            })
    },
    computed: {
        cartNum: function () {
            let n = this.$store.getters.getCartNum;
            return n > 0 ? n : '';
        },
        favorNum: function () {
            let n = this.$store.getters.getFavorNum;
            return n > 0 ? n : '';
        },
        bookCommentNum: function () {
            let n = this.$store.getters.getbookCommentNum;
            return n > 0 ? "(" + n + ")" : "(0)";
        },
        favor: function(){
            return this.details.etc.isFavor == 0 ? "收藏书籍" : "取消收藏"
        }
    }
};
</script>

<style scoped>
.van-nav-bar .van-icon,
.van-nav-bar__text {
    color: #1989fa;
}
.van-card__price {
    font-size: 16px;
}
.bookDetail {
    margin: 20px;
    height: auto;
    /* padding-bottom: 50px; */
    white-space: pre-wrap;
    text-align: center;
}

.tagFavor {
    bottom: 170px;
    background-color: #ee0a24;
}

.van-tag--large {
    margin-bottom: 5px;
}

.van-card__title {
    font-size: large;
    line-height: 25px;
    max-height: 50px;
    margin-bottom: 5px;
}

.van-card__desc {
    font-size: large;
    margin-bottom: 5px;
}

.detail {
    background-color: white;
    padding-bottom: 50px;
}

.van-card {
    text-align: left;
}
.comment {
    text-align: left;
    padding: 10px;
}
.book {
    text-align: left;
    padding: 15px;
    
}
.book div {
    margin-bottom: 10px;
}
.img {
    width:220px;
    height:220px;
}
.img2 {
    width:40px;
    height:40px;
}
.van-nav-bar {
    z-index: 0;
}
table {
    border-spacing: 15px;
}
</style>