<template>
    <div class="login">
        <van-nav-bar left-arrow @click-left=$router.go(-1) title="请登陆" >
            <van-icon name="ellipsis" slot="right" />
        </van-nav-bar>
        <div class="content">
            <van-image src="/head.png"></van-image>
            <van-field v-model="username" label="用户名" placeholder="用户名" />
            <van-field v-model="password" type="password" label="密码" placeholder="密码" @keyup.enter="submit()" />
            <van-button round type="info" size="large" @click="submit()" >提交</van-button>
            <div style="margin-top:20px">
                <router-link to="/register">没有账号？点击去注册</router-link>
            </div>
        </div>
    </div>
</template>
<script>
import api from '@/api'
import { Toast } from 'vant';

export default {
    inject: ['reload'],
    name: 'login',
    data: function () {
        return {
            username: this.$route.query.username,
            password: this.$route.query.password,
        }
    },
    methods: {
        submit() {
            let self = this;
            var data = {
                'username': this.username,
                'password': this.password
            }
            api.login("/user/login", data)
                .then(function (res) {
                    console.log(res);
                    if (res.statusCode == 200) {
                        Toast("登录成功");
                        self.$store.commit('setUser', {
                            Authorization: res.data.Authorization,
                            username: res.data.user.username,
                            nickname: res.data.user.nickname,
                        })
                        self.$store.commit('setCartNum')                       
                        self.$router.push({ path: '/frontUser' })
                    }
                    else if (res.statusCode == 600) { //账号或密码错误
                        Toast(res.message)
                        self.username = ""
                        self.password = ""
                    }
                }).catch(function (err) {
                    console.log("error:", err);
                })
        }
    }
}
</script>

<style scoped>
.login {
    background-color: white;
    padding-left: 15px;
    padding-right: 15px;
}

.content {
    margin: 0 auto;
}

.van-button--large {
    width: 70%;
}
</style>