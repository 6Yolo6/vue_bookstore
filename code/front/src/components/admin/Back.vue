<template>
  <div class="back">
    <el-container style="height: 1080px;
      border-width: 1px;
      border-style: solid;
      border-color: rgb(96, 131, 144) !important;
      border-image: initial;
      background-color: rgb(207, 207, 237) !important;
      color: rgb(0, 0, 0) !important;">
        <aside class="el-aside" style="overflow: hidden;width: 250px; background-color: rgb(34, 46, 60);">
            <my-aside></my-aside>
        </aside>
        <el-container class="el-container is-vertical" style="background-color: rgb(207, 207, 237) !important;">
            <header class="el-header"
                style="height: 60px; text-align: right; font-size: 12px; background-color: rgb(227, 230, 232) !important; color: rgb(55, 76, 83) !important;">
                <my-header></my-header>
            </header>
            <router-view></router-view>
        </el-container>
    </el-container>
  </div>
</template>

<script>
import api from "@/api";
import { Toast } from "vant";
import myAside from "./Aside.vue";
import myHeader from "./Header.vue";
export default {
    name: 'back',
    components: {
        "my-aside":myAside,
        "my-header":myHeader
    },
    data: function () {
        return {

        }
    },
    created: function () {
        let self = this;
        api.getUser("/user/getUser", localStorage.getItem('username'))
            .then(res => {
                console.log(res.data);
                if (res.data.user.privilege == 0) {
                    Toast("权限不足，请联系管理员")
                    self.$router.push({ path: '/frontUser' })
                }
            })
            .catch(err => {
                Toast("请重新登录")
                self.$router.push({ path : 'backLogin'})
            })
    }
}
</script>

<style scoped>

</style>