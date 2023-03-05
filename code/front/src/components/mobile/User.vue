<template>
    <div class="user">
        <div class="header">
            <div class="header1">
                <van-image round class="userImg" :src="nickname != null ? '/user.jpg' : ''" />
                <div class="setting" @click="toSetting()">
                    <van-icon size="25" name="setting-o" />
                    <div>设置</div>
                </div>
                <div v-if="nickname != null">
                    <div class="username">{{ nickname }}</div><br>
                    <div style="float:left;margin-top: 10px;margin-left: -50px;">
                        <span>金币 666 | </span>
                        <span>粉丝 666</span>
                    </div>
                </div>
                <div v-else>
                    您没有登陆，请登陆或注册<br>
                    <van-button round type="info" size="normal" @click="login()">去登陆</van-button>
                    <van-button round type="info" size="normal" @click="register()">去注册</van-button>
                </div>
            </div>

        </div>
        <div class="nav1">
            <van-row gutter="20" class="like">
                <van-col span="8" @click="toFavor()">
                    <van-icon name="star-o" />商品收藏
                </van-col>
                <van-col span="8" @click="toComment()">
                    <van-icon name="underway-o" />我的评价
                </van-col>
                <van-col span="8">
                    <van-icon name="cash-back-record" />我的零钱
                </van-col>
            </van-row>

        </div>
        <div class="nav2">
            <van-cell value="全部" @click="toOrder(0)" is-link class="order_cell">
                <template #title>
                    <span class="custom-title">我的订单</span>
                </template>
            </van-cell>
            <van-row class="order" type="flex" justify="space-between">
                <van-col span="4">
                    <van-icon name="underway-o" @click="toOrder(1)" :badge="toBeStatus(1)" />
                    <div>待付款</div>
                </van-col>
                <van-col span="4">
                    <van-icon name="cash-back-record" @click="toOrder(2)" :badge="toBeStatus(2)" />
                    <div>待发货</div>
                </van-col>
                <van-col span="4">
                    <van-icon name="star-o" @click="toOrder(3)" :badge="toBeStatus(3)" />
                    <div>待收货</div>
                </van-col>
                <van-col span="4">
                    <van-icon name="cash-back-record" @click="toOrder(4)" :badge="toBeStatus(4)" />
                    <div>待评价</div>
                </van-col>
                <van-col span="4">
                    <van-icon name="cash-back-record" @click="toRefund(5)" :badge="toBeStatus(5)" />
                    <div>退款</div>
                </van-col>
            </van-row>
        </div>
    </div>
</template>

<script>
import api from '@/api';


export default {
    name: 'frontUser',
    data: function () {
        return {
            nickname: localStorage.getItem('nickname'),
        }
    },
    computed: {
        toBeStatus() {
            return (status) => {
                let n = this.$store.getters.getOrderNum(status);
                return n  ? n : '';
            }
        },
    },
    created: function() {
        let name = localStorage.getItem('username');
        if (name != null) {
        let self = this;
        api.getUser("/user/getUser", name)
            .then(res => {
                let num = new Array(8).fill(0)
                if (res.statusCode == 200) {
                    self.user = res.data.user;
                    api.getByUserId("/order/getByUserId")
                        .then(res => {
                            console.log(res.data);
                            res.data.forEach(i => {
                                if(i.status == -1)
                                    num[6]++
                                else if(i.status == -2)
                                    num[7]++
                                else {
                                    num[i.status]++
                                    num[0] += num[i.status]  
                                }                               
                            })
                            console.log(num);
                            self.$store.commit('setOrderNum', num);
                            // self.$router.replace({ path: '/search' });//跳转空白页刷新
                        })
                }
                else {
                    self.$store.commit('setOrderNum', num);
                }
            })
        }
    },
    methods: {
        register() {
            this.$router.push('register');
        },
        login() {
            this.$router.push('login');
        },
        toOrder(status) {
            this.$router.push({ path: '/order', query: { status: status } });
        },
        toSetting() {
            this.$router.push({ path: '/setting', query: {user: this.user}});
        },
        toFavor() {
            this.$router.push({ path: '/favor', });
        },
        toComment() {
            this.$router.push({ path: '/myComment', });
        },
        toRefund(status) {
            this.$router.push({ path: '/reFund', query: { books: [], status: status } })
        }
    }
}
</script>

<style scoped>
.user {
    padding-top: 20px;
    background-color: #fffde4;
}

.header {
    height: 100px;
}

.header1 {
    padding: 10px;
}

.userImg {
    width: 4rem;
    height: 4rem;
    float: left;
}

.username {
    float: left;
    font-size: larger;
    margin-left: 20px;
}

.custom-title {
    float: left;
}


.setting {
    float: right;
}

.van-button--large {
    width: 70%;
}

.nav1 {
    background-color: #fff5cd;
    border-radius: 16px;
    padding: 10px;
    margin: 5px;
}

.nav2 {
    background-color: #f1f3d4;
    border-radius: 16px;
    padding: 5px;
    margin: 5px;
}

.like {
    margin-bottom: 5px;
}

.order {
    margin: 5px 20px 5px 20px;
}

.order i {
    font-size: 30px;
}

.order_cell {
    background-color: #f1f3d4;
    border-radius: 16px;
    padding-left: 8px;
    padding-right: 8px;
}
</style>