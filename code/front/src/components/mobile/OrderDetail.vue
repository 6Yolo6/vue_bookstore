<template>
    <div class="orderDetail">
        <van-nav-bar :left-text="orderStatus(this.book)" left-arrow @click-left=$router.go(-1)>
            <template #right>
                <van-icon size="25" name="ellipsis" />
            </template>
        </van-nav-bar>
        <div class="content">
            <div class="publish">
                <nav>{{ book.publisher }}</nav>
            </div>
            <van-card :num="book.num" :price="book.price" :desc="book.author" :title="book.name"
                :thumb="'/' + book.img">
            </van-card>
            <div class="info">
                <div>
                    <div class="infoLeft">收货地址</div>
                    <div class="infoRight">{{ book.etc.address }}</div>
                </div>
                <div>
                    <div class="infoLeft">订单编号</div>
                    <div class="infoRight">{{ book.etc.no }}</div>
                </div>
                <div>
                    <div class="infoLeft">创建时间</div>
                    <div class="infoRight">{{ book.etc.createTime }}</div>
                </div>
                <div v-if="book.status == -2">
                    <div class="infoLeft">取消时间</div>
                    <div class="infoRight">{{ new Date(book.etc.cancelTime).toLocaleString() }}</div>
                </div>
                <div v-if="book.status == -2">
                    <div class="infoLeft">取消原因</div>
                    <div class="infoRight">{{ book.etc.cancelReason }}</div>
                </div>
            </div>
            <van-button v-if="this.book.status != -2 && this.book.status != 5" style="margin-right:20px"
                round type="info" @click="action()">
                {{ orderAction(this.book.status) }}
            </van-button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'orderDetail',
    data: function () {
        return {
            book: [],
        }
    },
    created: function () {
        // console.log(this.$route.query.book);
        this.book = this.$route.query.book;
    },
    methods: {
        action() {
            let self = this;
            let status = this.book.status
            console.log(status);
            this.$dialog.confirm({
                title: this.orderAction(status),
                message: '是否继续？',
            })
                .then(() => {
                    // on confirm
                    if (status == 4 || status == -1) {//评价、追评
                        self.$router.push({ path: '/comment', query: { book: self.book } });
                    }
                    else if (status == 2) {
                        console.log("666");
                    }
                    else {
                        api.updateOrderDetail("/orderDetail/update", self.book.orderId, self.book.bookId, self.book.status)
                        .then(res => {
                            console.log(res);
                            self.$router.push({path: '/order', status: self.book.status})
                        })
                    }
                })
                .catch(() => {
                    // on cancel
                });
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
        orderAction(status) {
            let action = ['继续付款','催发货','确认收货','评价']
            if (status == -1)
                return "追评"
            else
                return action[status-1]
        }
    }
}
</script>

<style scoped>
.content {
    text-align: left;
}

.publish {
    margin-left: 15px;
    padding-top: 10px;
}

.info {
    padding: 10px;
    display: grid;
}
.info div {
    margin-bottom: 10px;
}
.infoLeft {
    float: left;
}

.infoRight {
    float: right;
    white-space: normal;
    word-break: break-all;
    word-wrap: break-word;
    width: 180px;
}

.van-button--info {
    color: rgb(255 157 129);
    background-color: #ffffff;
    border: 1px solid rgb(255 157 129);
    float: right;
}
</style>