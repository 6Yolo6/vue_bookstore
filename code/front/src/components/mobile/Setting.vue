<template>
    <div class="setting">
        <van-nav-bar left-text="设置" left-arrow @click-left=$router.back(-1)>
            <template #right>
                <van-icon size="25" name="ellipsis" />
            </template>
            
        </van-nav-bar>
        <van-nav-bar>
            <template #right>
                <van-icon name="arrow" />
            </template>
            <template #left>
                <van-image round class="userImg" :src="user.username != null ? '/user.jpg' : ''" />
                <div v-if="user.username != null" class="username">{{ user.username }}</div>
            </template>
        </van-nav-bar>
        <van-nav-bar left-text="我的收货地址" @click-left="toAddress()" style="margin-top: 20px;">
            <template #right>
                <van-icon name="arrow" />
            </template>
        </van-nav-bar>
        <van-nav-bar v-if="(user.privilege == 1)" left-text="管理后台" @click-left="toBack()" style="margin-top: 20px;">

        </van-nav-bar>
        <van-button style="margin-top: 20px;" size="large" @click="loginOut()">退出登陆</van-button>
        
    </div>
</template>

<script>
import api from '@/api';
export default {
    name: 'setting',
    data: function () {
        return {
            user: {},
        }
    },
    methods: {
        toAddress() {
            this.$router.push('address');
        },
        toBack() {
            this.$router.push({path: '/myIndex'});
        },
        loginOut() {
            // this.$store.state.user = '';
            this.user = '';
            this.$store.state.favorNum = ''
            this.$store.state.orderNum = ''
            this.$store.state.cartNum = ''
            this.$store.state.commentNum = ''
            localStorage.removeItem('username');
            localStorage.removeItem('nickname');
            localStorage.removeItem('Authorization');
            // console.log(this.username);
            this.$router.push('login');
        },
    },
    created: function() {
        let self = this;
        api.getUser("/user/getUser", localStorage.getItem('username'))
            .then(res => {
                self.user = res.data.user;
                
            })
    }
}
</script>

<style scoped>

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
</style>