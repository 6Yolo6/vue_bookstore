<template>
    <el-row style="position: relative;">
              <el-col :span="8">
                <el-input class="el-input" placeholder="搜索..." size="small" style="padding-top: 15px;">
                </el-input>
                <span class="el-input__suffix">
                  <i class="el-input__icon el-icon-search"></i>
                </span>
              </el-col>
              <el-col :span="16" style="margin-top: 20px;">
                <el-dropdown size="small" style="color: rgb(61, 85, 92) !important;">
                  <i class="el-icon-setting el-dropdown-selfdefine" style="margin-right: 15px;"></i>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click.native="toFront()">浏览前台</el-dropdown-item>
                      <el-dropdown-item @click.native="loginOut()">退出登陆</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
                <span>{{username}}</span>
              </el-col>
            </el-row>
</template>

<script>
import api from '@/api';
export default {
    name: "header",
    data: function () {
        return {
          username: localStorage.getItem('username')
        }
    },
    methods: {
      toFront() {
        this.$router.push({path: '/frontUser'});
      },
      loginOut() {
            // this.$store.state.user = '';
            
            localStorage.removeItem('username');
            localStorage.removeItem('nickname');
            localStorage.removeItem('Authorization');
            // console.log(this.username);
            this.$router.push({path: '/backLogin'});
      },
    },
    created: function() {
      
    }
}
</script>

<style scoped>
.el-input__suffix {
  position: relative;
  height: 100%;
  top: -35px;
  text-align: center;
  color: #c0c4cc;
  transition: all .3s;
  pointer-events: none;
}
</style>