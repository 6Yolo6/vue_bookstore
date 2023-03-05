<template>
    <div class="order">
        <van-nav-bar left-arrow @click-left=back()>
            <van-search v-model="searchValue" placeholder="请输入搜索关键词" slot="title" />
            <van-icon name="filter-o" slot="right" />
            <span style="margin-right: 10px;" slot="right">筛选</span>
            <van-icon name="ellipsis" slot="right" />
        </van-nav-bar>
        <van-tabs :active="active" @click="change" animated>
            <van-tab v-for="index in 6" :key="index" :name="index" :title="orderTitle(index)">
                <div v-for="publisher, indexP in orders" :key="indexP" style="text-align: left;">
                    <div class="publish" v-if="publisher.books[0].status == active-1 || active == 1 || publisher.books[0].status == -1">
                        <div style="float:left">{{ publisher.publisher }}</div>
                    </div>
                    <div v-for="book, indexB in publisher.books" :key="indexB">
                        <!-- {{indexB}} -->
                        <div v-if="(book.status == active-1 || active == 1 || book.status == -1)">
                            <van-card :num="book.num" :price="book.price" :desc="book.author"
                                :title="book.name" :thumb="'/' + book.img" @click="toDetail(book)">
                                <template #tag>
                                    <div class="orderNo">
                                        {{ '订单编号:' + book.etc.no }}</div>
                                    <div class="orderstatus">{{ orderStatus(book) }}</div>
                                </template>
                                <template #footer>
                                    <van-button v-if="book.status == 2" @click="cancelOrder(book.orderId, index)" round type="info">取消订单</van-button>
                                    <van-button v-if="book.status == -2 || book.status == 5" @click="deleteOrderDetail(book)" round type="info">删除订单</van-button>
                                    <van-button v-if="book.status != -2 && book.status != 5" round type="info"
                                        @click="action(book, book.status)">{{ userAction(book.status) }}
                                    </van-button>
                                </template> 
                            </van-card>
                            <van-count-down v-if="book.status == 1" :time="time(book)" @finish="finish(book)">
                                <template #default="timeData">
                                    <span>剩余时间 </span>
                                    <span class="block">{{ timeData.hours }}</span>
                                    <span class="colon">:</span>
                                    <span class="block">{{ timeData.minutes }}</span>
                                    <span class="colon">:</span>
                                    <span class="block">{{ timeData.seconds }}</span>
                                </template>
                            </van-count-down>
                            <!-- <van-count-down ref="countDown" millisecond :time="3000" :auto-start="true" format="ss:SSS"  /> -->
                        </div>
                    </div>
                </div>
            </van-tab>
        </van-tabs>
        <van-action-sheet v-model="show" close-on-click-action>
            <template slot="default">
                <div style="padding:10px">
                    <van-field style="border-radius: 16px;" v-model="reason" rows="4" autosize label="取消理由" type="textarea" maxlength="60"
                    placeholder="请输入理由" show-word-limit />
                    <van-button style="float:right;margin-bottom: 10px;" @click="cancel()" type="primary">提交</van-button>
                </div>
            </template>
        </van-action-sheet>
        <!-- <van-loading type="spinner" color="#1989fa" /> -->
    </div>
</template>

<script>

import api from '@/api';
import { Toast } from 'vant';
export default {
    name: 'order',
    inject: ['reload'],
    data: function () {
        return {
            active: 0,
            searchValue: '',
            orders: [],
            load: false,
            reason: '',
            show: false,
            i: '',
            orderId: '',
        }
    },
    created: function () {
        console.log(this.$route.query.status);
        let status = parseInt(this.$route.query.status)+1;
        // console.log(status);
        this.getOrder(status);
        this.active = status;
    },
    methods: {
        orderTitle(index) {
            let title = ['全部', '待付款', '待发货', '待收货', '待评价', '已评价'];
            return title[index - 1];
        },
        orderStatus(book) {
            let title = ['等待买家付款', '买家已付款', '卖家已发货', '交易成功', '退款/售后'];
            if (book.status == -1)
                return '已评价'
            else if (book.status == -2) {
                if (book.etc.cancelReason == "超时自动取消")
                    return '超时取消'
                else
                    return '交易关闭'
            }               
            else
                return title[book.status-1];
        },
        userAction(index) {
            let title = ['继续付款', '催发货', '确认收货', '评价'];
            if (index == -1)
                return "追评"
            else
                return title[index -1];
        },
        finish(book) {
            Toast("订单"+book.etc.no+"超时自动取消")
            this.getOrder(1)
            this.reload()
        },
        back() {
            this.$router.push({path: '/frontUser'});
        },
        change(index) {
            // 
            
            this.getOrder(index);
            this.$router.push({ path: '/order', query: { status: index == 6 ? -1 : index - 1 } });
        },
        time(book) {
            console.log(book.etc.cancelTime, "取消时间")
            // console.log(Date.now());
            return book.etc.cancelTime - Date.now()
        },
        cancelOrder(orderId, index) {
            event.stopPropagation();
            this.show = true
            this.orderId = orderId
            this.i = index
            console.log(index)
        },
        cancel() {
            //cancelType--1 用户取消
            //cancelType--2 超时取消
            // if (cancelType == 2)
            //     this.reason = "超时自动取消"
            let self = this;
            api.cancelOrder("/order/cancel", this.orderId, this.reason)
                .then(res => {
                    console.log(res)
                    Toast("取消成功")
                    
                    self.show = false
                    self.getOrder(self.i-1)
                })
        },
        deleteOrderDetail(book) {
            event.stopPropagation()
            // console.log("book", book);
            let self = this
            this.$dialog.confirm({
                title: "删除订单",
                message: '是否继续？',
            })
                .then(() => {
                    api.deleteOrderDetail("/orderDetail/deleteByIds", book.orderId, book.bookId)
                        .then(res => {
                            console.log("删除订单", res.data);
                            Toast("删除成功")
                            self.getOrder(1)
                        })
                })
                .catch(() => {
                    // on cancel
                });
        },
        action(book, status) {
            event.stopPropagation();
            // this.load = true;
            // setTimeout(function () {
            //     this.load = false;
            // }, 1000)
            console.log(status);
            if (status != 2) {
                let self = this;
                this.$dialog.confirm({
                    title: this.userAction(status),
                    message: '是否继续？',
                })
                    .then(() => {
                        // on confirm
                        if (status == 4 || status == -1) {//评价、追评
                            self.$router.push({ path: '/comment', query: { book: book } });
                        }
                        else {
                            api.updateOrderDetail("/orderDetail/update", book.orderId, book.bookId, book.status)
                                .then(res => {
                                    console.log(res);
                                    self.getOrder(status);
                                    self.reload()
                                })
                        }
                    })
                    .catch(() => {
                        // on cancel
                    });
            } 
        },
        toDetail(book) {
            if (book.status == 5)
                this.$router.push({ path: '/reFund', query: { books: book } });
            else
                this.$router.push({ path: '/orderDetail', query: { book: book } });
        },
        getOrders(orders) {//按照出版社分类
            let publisher = [];
            orders.forEach(i => {
                publisher.push(i.publisher);
            })
            // console.log(publisher);
            //去重出版社
            publisher = Array.from(new Set(publisher));
            // console.log(publisher);

            //不同出版社
            let list = [];
            for (let i = 0; i < publisher.length; i++) {
                list.push({ publisher: publisher[i], books: [] });
                let item = [];
                orders.forEach(book => {
                    if (book.publisher == publisher[i])
                        item.push(book);
                })
                // console.log(item);

                list[i].books = item;
            }
            return list;
        },
        getOrder(index) {          
            this.active = index;
            if (index == 6)
                index = 0
            let status = index - 1;
            // console.log(status);
            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(function (res1) {
                    // console.log(res1.statusCode);
                    if (res1.statusCode == 200) {
                        api.getByUserId("/order/getByUserId")
                            .then(res => {
                                console.log(res.data);
                                let orderIds = [];
                                res.data.forEach(i => {
                                    orderIds.push(i.id);
                                })
                                console.log(orderIds);
                                api.getDetail("/orderDetail/getDetail", orderIds.join(","), status)
                                    .then(res1 => {
                                        if (res1.statusCode == 200) {
                                        console.log(res1.data);
                                        
                                        let books = [];
                                        let carts = [];
                                        carts = res1.data;

                                        let bookIds = [];
                                        carts.forEach(i => {
                                            bookIds.push(i.bookId);

                                        })
                                        // console.log(bookIds.join(","));
                                        api.getCartBookByIds("/book/getBookByIds", bookIds.join(","))
                                            .then(function (res2) {
                                                //合并数组
                                                console.log(res2.data);
                                                books = carts.map((item, index) => {
                                                    return { ...res2.data[index], ...item };
                                                });
                                                console.log(books);
                                                self.orders = self.getOrders(books);
                                                console.log(self.orders);
                                            })
                                        }
                                    })
                            })
                    }
                    else if (res1.statusCode == 401) {
                        Toast("token过期");
                        self.$router.push('login');
                    }
                })
        }
    }
}
</script>

<style scoped>
.publish {
    margin-left: 5px;
    padding: 15px;
    line-height: 15px;
    margin-bottom: 15px;
}

.van-card__tag {
    left: 290px;
    width: 85px;
    top: 0px;
}

.van-card__title {
    white-space: normal;
    word-break: break-all;
    word-wrap: break-word;
    width: 180px;
}

.van-card {
    margin-bottom: 20px;
}

.van-button--info {
    color: #000;
    background-color: #ffffff;
    border: 1px solid #a6adb5;
}

.van-loading {
    position: absolute;
    left: 175px;
    background-color: white;
}
.orderNo {
    position: relative;
    bottom: 20px;
    right: 290px;
    width: 285px;
}
.orderstatus {
    color: #30cbce;
    font-size: medium;
    top: 10px;
    right: 20px;
    position: relative;
}
.van-count-down {
    position: relative;
    bottom: 20px;
    left: 20px;
}
.colon {
    display: inline-block;
    margin: 0 4px;
    color: #ee0a24;
  }
  .block {
    display: inline-block;
    width: 22px;
    color: #fff;
    font-size: 12px;
    text-align: center;
    background-color: #ee0a24;
  }
</style>