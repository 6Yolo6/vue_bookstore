<template>
    <div class="ConfirmOrder">
        <van-nav-bar left-text="确认订单" left-arrow @click-left=$router.go(-1) />
        <!-- <van-contact-card :type="addressType" add-text="添加收货地址" :name="user.username" :tel="user.telephone" @click="onAdd" >
        </van-contact-card> -->
        <van-address-list :list="list" style="padding: 0px" @click-item="onAdd()" default-tag-text="默认" />
        <div v-for="publisher,indexP in carts" :key="indexP" style="text-align: left;">
            <div class="publish">
                <nav >{{publisher.publisher}}</nav>
            </div>
            <van-card v-for="book,indexB in publisher.books" :num="book.num" :key="indexB" :price="book.price" :desc="book.author"
                :title="book.name" :thumb="'/' + book.img" >
            
            </van-card>
        </div>
        <van-action-sheet v-model="show" close-on-click-action>
            <template slot="default">
                <van-address-edit :address-info="address" :area-list="areaList" show-delete
                   :area-columns-placeholder="['请选择', '请选择', '请选择']" @save="onSave" @delete="onDelete" />
            </template>
        </van-action-sheet>
        <van-submit-bar :suffix-label="totalNum" :price="parseInt($route.query.totalPrice)" button-text="提交订单" @submit="submit">
            
        </van-submit-bar>
    </div>
</template>

<script>
import api from "@/api"
import { Toast } from "vant";
import { areaList } from '@vant/area-data';

export default {
    name: 'confirmOrder',
    data: function () {
        return {
            books: [],
            carts: [],
            user: {},
            show: false,
            areaList: areaList,
            searchResult: [],
            address: {},
            list: [],
        }
    },
    methods: {
        getUser() {
            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(function (res) {
                    console.log(res.data);
                    self.user = res.data.user;
                    self.list = [];
                    self.list.push({name: self.user.username, tel: self.user.telephone, address: self.user.address});
                })
        },
        onEdit() {
            this.$router.push('address');
        },
        submit() {
            // this.$route.query.totalPrice
            let bookIds = [];
            let numIds = [];
            this.carts.forEach(cart => {
                cart.books.forEach(book => {
                    bookIds.push(book.bookId);
                    numIds.push(book.num);
                })
            })
            console.log(bookIds.join(","));
            console.log(numIds.join(","));
            let self = this;
            this.$dialog.confirm({
                title: '订单结算',
                message: '是否付款',
            })
                .then(() => {
                    // on confirm
                    api.addOrder("/order/add", this.$route.query.totalPrice / 100, 1)
                        .then(res => {
                            console.log(res.data);
                            let data = {
                                orderId: res.data.id,
                                status: 2,
                                ids: bookIds.join(","),
                                nums: numIds.join(","),
                            }
                            api.addOrderDetail("/orderDetail/add", data)
                                .then(res1 => {
                                    console.log(res1.data);
                                    api.deleteByIds("/cart/deleteByIds", bookIds.join(","))
                                        .then(res2 => {
                                            console.log(res2.data);
                                            Toast("成功");
                                            self.$router.push({ path: '/order', query: { status: 2 } });
                                        })
                                })
                        })
                })
                .catch(() => {
                    // on cancel
                    api.addOrder("/order/add", this.$route.query.totalPrice / 100, 0)
                        .then(res => {
                            console.log(res.data);
                            let data = {
                                orderId: res.data.id,
                                status: 1,
                                ids: bookIds.join(","),
                                nums: numIds.join(","),
                            }
                            api.addOrderDetail("/orderDetail/add", data)
                                .then(res1 => {
                                    console.log(res1.data);
                                    api.deleteByIds("/cart/deleteByIds", bookIds.join(","))
                                        .then(res2 => {
                                            console.log(res2.data);
                                            Toast("成功");
                                            self.$store.commit('setCartNum')
                                            self.$router.push({ path: '/order', query: { status: 1 } });
                                        })
                                })
                        })
                });
        },
        onAdd() {
            this.show = true;
            this.address = {name: this.user.username, tel: this.user.telephone};
        },
        onSave(myAddress) {
            console.log(myAddress);
            let address = myAddress.province + myAddress.city + myAddress.county + " " + myAddress.addressDetail;
            
            var data = {
                'telephone': myAddress.tel,
                'address': address
            }
            let self = this;
            api.updateUser("/user/updateUser", data)
                .then(function(res) {
                    console.log(res.data.data);
                    self.user = res.data.data;
                    self.show = false;
                    self.getUser();
                })
        },
        onDelete(myAddress) {
            myAddress.tel = '';
            myAddress.province = '';
            myAddress.name = '';
            myAddress.county = '';
            myAddress.city = '';
            myAddress.areaCode = '';
            myAddress.addressDetail = '';
        }
    },
    created: function() {
        this.carts = this.$route.query.books;
        // console.log(this.$route.query.totalPrice);

        this.getUser();
    },
    computed:  {
        totalNum: function() {
            let num = 0;
            this.books.forEach(i => {
                num += i.num;
            })
            return num > 0 ? '共'+num+'件': '';
        },
    }

}
</script>

<style scoped>

.van-nav-bar .van-icon {
    color: black !important;
}
.van-nav-bar__text {
    color: black !important;
}
.publish {
    margin-left: 15px;
    padding: 10px;
}

.van-address-list__bottom {
    display: none;
}
.van-address-edit__buttons {
    padding: 0px;
}
</style>