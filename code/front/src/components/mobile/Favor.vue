<template>
    <div class="favor">
        <van-nav-bar left-arrow @click-left=$router.back(-1) :title="'我的收藏'+favorNum">
            <!-- <van-search v-model="searchValue" placeholder="请输入搜索关键词" slot="title" /> -->
            <van-icon style="margin-right: 10px;" name="search" slot="right" />
            <span style="margin-right: 10px;" slot="right">管理</span>
        </van-nav-bar>
        <van-notice-bar v-if="this.$store.getters.getFavorNum == ''">您的收藏空空如也...</van-notice-bar>
        <div class="content">
            <van-card v-for="book, index in books" :key="index" :price="book.etc.book.price" :desc="book.etc.book.author"
                :title="book.etc.book.name" :thumb="'/' + book.etc.book.img" @click="toDetail(book.bookId)">
                <template #tags>
                    <van-tag plain size="large" type="primary">{{ book.etc.book.publisher }}</van-tag>
                    <br>
                    <van-tag type="danger">{{ book.etc.cateName }}</van-tag>
                </template>
                <template #tag>
                    <van-checkbox v-model="checkedIds[index]" @click.native="updateFavor(index, 0)" />
                </template>
                <template #num>
                    <van-icon name="delete-o" @click="deleteFavor(book.bookId)" />
                </template>
            </van-card>
            <van-submit-bar button-text="删除" @submit="deleteFavors()">
                <van-checkbox style="width: 70%;" v-model="allCheck" @click.native="updateFavor(0, 1)">全选</van-checkbox>
            </van-submit-bar>
        </div>
    </div>
</template>

<script>
import api from '@/api'

export default {
    name: 'favor',
    data: function () {
        return {
            searchValue: '',
            books: [],
            ids: [],
            allCheck: '',
            checkedIds: [],
            no: 0
        }
    },
    methods: {
        checkAll() {
            let flag = 0;
            for (let i = 0; i < this.checkedIds.length; i++) {
                if (this.checkedIds[i] == false) {
                    this.allCheck = false;
                    break;
                }
                else
                    flag++;
                if (flag == this.books.length)
                    this.allCheck = true;
            }
        },
        updateFavor(index, flag) {
            event.stopPropagation();
            if (this.no == 0) {
                for (let i = 0; i < this.books.length; i++)
                    this.checkedIds[i] = false;
                this.no++;
            }
            if (flag == 0) {
                this.checkedIds[index] = !this.checkedIds[index];
            }
            else if (flag == 1) {
                let cnt = 0;
                for (let i = 0; i < this.books.length; i++) {
                    if (this.checkedIds[i] == true)
                        cnt++;
                }
                if (cnt == this.books.length) {//全反选
                    for (let i = 0; i < this.books.length; i++)
                        this.checkedIds[i] = false;
                }
                else {//全选
                    for (let i = 0; i < this.books.length; i++)
                        if (this.checkedIds[i] == false)
                            this.checkedIds[i] = true;
                }
            }
            this.checkAll();
        },
        toDetail(id) {
            this.$router.push({ name: 'detail', query: { id: id } });
        },
        deleteFavor(ids) {//删除
            let self = this;
            event.stopPropagation();
            this.$dialog.confirm({
                title: '删除商品',
                message: '确认删除吗？',
            })
                .then(() => {
                    // on confirm
                    console.log("delete");
                    api.deleteByIds("/favor/deleteByIds", ids)
                        .then(function (res) {
                            // console.log(res.data);
                            self.getFavor();
                        })
                    
                    // self.reload();
                })
                .catch(() => {
                    // on cancel

                });
        },
        deleteFavors() {
            let ids = [];
            for (let i = 0; i < this.books.length; i++)
                if (this.checkedIds[i] == true)
                    ids.push(this.books[i].bookId);
            console.log(ids, "ids");
            if (ids.length != 0)
                this.deleteFavor(ids.join(","));  
        },
        getFavor() {
            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(function (res1) {
                    // console.log(res1.statusCode);
                    if (res1.statusCode == 200) {
                        api.getBooksByUserId("/favor/getByUserId", res1.data.user.id)
                            .then(function (res2) {
                                // self.books = [];
                                // self.favors = [];
                                self.books = res2.data;
                                self.$store.dispatch('setFavorNum');
                                self.checkAll();
                            })
                    }
                    else if (res1.statusCode == 401) {
                        Toast("token过期");
                        self.$router.push('login');
                    }
                })
        }
    },
    created: function () {
        this.getFavor();
        this.checkedIds.fill(false);
        this.checkAll();
    },
    computed: {
        favorNum: function () {
            let n = this.$store.getters.getFavorNum;
            return n > 0 ? "(" + n + ")" : '';
        }
    }
}
</script>

<style scoped>
.favor {
    padding: 10px;
}

.content {
    text-align: left;
}

.van-submit-bar {
    bottom: 50px;
}

.van-card__tag {
    top: 30px;
    left: -20px;
}

.van-card__num {
    position: relative;
    bottom: 40px;
    left: 10px;
}
</style>