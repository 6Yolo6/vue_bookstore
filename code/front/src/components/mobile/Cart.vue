<template>
    <div class="cart">
        <van-nav-bar left-arrow @click-left=$router.back(-1) title="购物车">
            <van-icon v-if="!isManage" name="ellipsis" slot="right" @click="manage()" />
            <span v-else slot="right" @click="manage()">退出管理</span>
        </van-nav-bar>
        <van-notice-bar v-if="this.$store.getters.getCartNum == ''">您的购物车空空如也...</van-notice-bar>
        <div class="content">
            <div v-for="publisher, indexP in shopCarts" :key="indexP">
                <div class="publish">
                    <nav>{{ publisher.publisher }}</nav>
                </div>
                <van-card v-for="book, indexB in publisher.books" :key="indexB" :price="book.price" :desc="book.author"
                    :title="book.name" :thumb="'/' + book.img" @click="toDetail(book.bookId)">
                    <template #tag>
                        <van-checkbox v-model="book.checked" @click.native="updateCart(book.bookId, 0)" />
                    </template>
                    <template #num>
                        <van-icon name="delete-o" @click="deleteCart(book.bookId)" />
                    </template>
                    <template #footer>
                        <van-stepper theme="round" :value="book.num" @plus="addNum(book.bookId)"
                            @minus="subNum(book.bookId)" />
                    </template>
                </van-card>
            </div>
        </div>
        <van-submit-bar v-if="!this.isManage" :price="totalPrice" :disabled="isCheckCart" button-text="结算"
            @submit="submit(totalPrice)">
            <van-checkbox v-model="allCheck" @click.native="updateCart(0, 1)">全选</van-checkbox>
        </van-submit-bar>
        <van-submit-bar v-else button-text="删除" :disabled="isCheckCart" @submit="deleteCarts()">
            <van-checkbox style="width: 70%;" v-model="allCheck" @click.native="updateCart(0, 1)">全选</van-checkbox>
        </van-submit-bar>
    </div>
</template>

<script>
import api from "@/api"
import { Toast } from "vant";

export default {
    inject: ['reload'],
    name: 'cart',
    data: function () {
        return {
            books: [],
            ids: [],
            carts: [],
            shopCarts: [],
            allCheck: '',
            isManage: false,
        }
    },
    computed: {
        totalPrice: function () {
            let total = 0;
            this.books.forEach(i => {
                if (i.checked == 1) {
                    total += i.num * i.price * 100;
                }
            })
            return total;
        },
        isCheckCart: function () {
            let flag = true;
            this.books.forEach(item => {
                if (item.checked == 1 || item.checked == true)
                   flag = false;
            })
            // console.log(flag);
            return flag;
        }
    },
    methods: {
        addNum(id) {//数量增加
            event.stopPropagation();
            api.add("/cart/addById", id)
                .then(function (res) {
                    // console.log(res.data);
                })
        },
        subNum(id) {//数量减少
            event.stopPropagation();
            api.sub("/cart/subById", id)
                .then(function (res) {
                    // console.log(res.data);
                })
        },
        checkAll() {
            let flag = 0;
            for (var item of this.books) {
                if (item.checked == 0) {
                    this.allCheck = false;
                    break;
                }
                else
                    flag++;
                if (flag == this.books.length)
                    this.allCheck = true;
            }
        },
        updateCart(ids, flag) {
            event.stopPropagation();
            let self = this;
            if (flag == 0) {
                api.updateByIds("/cart/updateByIds", ids)
                    .then(res => {
                        // console.log(res.data);
                        self.checkAll();
                    })
            }
            else if (flag == 1) {
                // this.$refs.checkboxGroup.toggleAll(!this.allCheck);
                api.updateByIds("/cart/updateByIds", this.ids.join(","))
                    .then(res => {
                        // console.log(res.data);
                        self.getCart();
                        self.checkAll();
                        // this.reload();
                    })
                // console.log(this.allCheck);
            }
        },
        deleteCart(ids) {//删除
            let self = this;
            event.stopPropagation();
            console.log(ids);
            this.$dialog.confirm({
                title: '删除商品',
                message: '确认删除吗？',
            })
                .then(() => {
                    // on confirm                    
                    api.deleteByIds("/cart/deleteByIds", ids)
                        .then(res => {
                            console.log(res.data);
                            self.getCart();
                            self.reload()
                        })
                    
                    // self.reload();
                })
                .catch((error) => {
                    // on cancel
                    console.log(error);
                });
        },
        deleteCarts() {
            let checkedIds = [];
            this.books.forEach(i => {
                if (i.checked == 1)
                    checkedIds.push(i.bookId);
            })
            console.log(checkedIds);
            this.deleteCart(checkedIds.join(","));
        },
        toDetail(id) {
            this.$router.push({ path: '/detail', query: { id: id } });
        },
        getCarts(carts) {//选中结算
            let publisher = [];
            carts.forEach(i => {
                publisher.push(i.publisher);
            })
            //去重出版社
            publisher = Array.from(new Set(publisher));
            // console.log(publisher);
            //不同出版社
            let list = [];
            for (let i = 0; i < publisher.length; i++) {
                list.push({ publisher: publisher[i], books: [] });
                let item = [];
                carts.forEach(book => {
                    if (book.publisher == publisher[i])
                        item.push(book);
                })
                // console.log(item);
                list[i].books = item;
            }
            return list;
        },
        getCart() {
            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(function (res1) {
                    // console.log(res1.statusCode);
                    if (res1.statusCode == 200) {
                        api.getBooksByUserId("/cart/getByUserId", res1.data.user.id)
                            .then(function (res2) {
                                self.books = [];
                                self.carts = [];
                                
                                if (res2.data) {
                                    self.carts = res2.data;                                   
                                    self.ids = [];
                                    self.carts.forEach(i => {
                                        self.ids.push(i.bookId);
                                    })
                                    // console.log(self.ids.join(","));
                                    api.getCartBookByIds("/book/getBookByIds", self.ids.join(","))
                                        .then(function (res3) {
                                            //合并cart数组
                                            self.books = self.carts.map((item, index) => {
                                                return { ...item, ...res3.data[index] };
                                            });
                                            // console.log(list);
                                            self.shopCarts = self.getCarts(self.books);
                                            self.checkAll();
                                            console.log(self.shopCarts);
                                        })
                                }
                            })
                    }
                    else if (res1.statusCode == 401) {
                        Toast("token过期");
                        self.$router.push('login');
                    }
                })
        },
        manage() {
            this.isManage = !this.isManage;
        },
        submit(totalPrice) {
            // console.log(totalPrice / 100);
            let checkCarts = [];
            this.books.forEach(i => {
                if (i.checked == 1 || i.checked == true)
                    checkCarts.push(i);
            })
            this.$router.push({ path: '/confirmOrder', query: { books: this.getCarts(checkCarts), totalPrice: totalPrice } });
        }
    },
    mounted: function () {
        if (localStorage.getItem('username')) {
            this.getCart();
            this.$store.dispatch('setCartNum');
            this.checkAll();
        }
        // this.getCart()
    }
}
</script>

<style scoped>
.cart {
    padding: 10px;
    background-color: white;
}

.content {
    text-align: left;
}

.van-card__bottom {
    margin-bottom: 30px;
}

.van-submit-bar {
    bottom: 50px;
}

.van-stepper {
    position: relative;
    bottom: 15px;
}

.van-card__tag {
    top: 30px;
    left: -20px;
}

.publish {
    margin-left: 15px;
    padding: 10px;
}

.van-card__num {
    position: relative;
    bottom: 40px;
    left: 10px;
}
</style>