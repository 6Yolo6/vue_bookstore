<template>
  <div class="register">
    <van-nav-bar left-arrow @click-left=$router.go(-1) title="请注册">
      <van-icon name="ellipsis" slot="right" />
    </van-nav-bar>
    <div class="content">
      <van-image src="/head.png"></van-image>
      <van-field left-icon="user-o" required v-model="username" @input="checkName()" label="用户名" placeholder="用户名" />
      <div class="tip">{{tip}}</div>
      <van-field left-icon="user-o" v-model="nickname" label="昵称" placeholder="昵称" />
      <van-field left-icon="smile-o" v-model="password" type="password" label="密码" placeholder="密码" />
      <van-field left-icon="smile-o" v-model="password2" @input="checkPass()" type="password" label="确认密码"
        placeholder="确认密码" />
        <div class="tip">{{tip2}}</div>
      <van-checkbox style="top: 30px;position: relative;" v-model="agree" shape="square">同意用户协议</van-checkbox>
      <van-button round type="info" :disabled="!agree" size="large" @click="submit()">注册</van-button>
      <div style="margin-top:20px">
                <router-link to="/login">已有账号？点击去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api'
import { Toast } from 'vant';


export default {
  name: 'register',
  data: function () {
    return {
      username: '',
      nickname: '',
      password: '',
      password2: '',
      tip: '',
      tip2: '',
      agree: false
    }
  },
  methods: {
    checkName() {
      var flag = /^[-_a-zA-Z0-9]{4,16}$/;
      if (this.username == '')
        this.tip = "用户名不能为空";
      else if (!flag.test(this.username))
        this.tip = "用户名需为4到16位，字母数字下划线，减号"
      else
        this.tip = ''
    },
    checkPass() {
      if (this.password2 != this.password)
        this.tip2 = "密码不一致";
      else
        this.tip2 = '';
    },
    submit() {
      let self = this;
      var data = {
        'username': this.username,
        'password': this.password,
        'nickname': this.nickname
      }
      api.register("/user/register", data)
        .then(res => {
          console.log(res);
          Toast(res.message)
          if(res.statusCode == 600) {//已注册
            self.username = ""
            self.agree = false
          }
          else
            self.$router.replace({ path: '/login', query: { username: data.username, password: data.password } });
        }).catch(function (err) {
          console.log("error:", err);
        })
    }
  }
}
</script>

<style scoped>
.register {
  background-color: white;
  padding-left: 15px;
  padding-right: 15px;
}

.content {
  margin: 0 auto;
}

.van-button--large {
    width: 45%;
    left: 80px;
}
.tip {
  font-size: 12px;
  color: red;
}
</style>