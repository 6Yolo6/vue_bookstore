<template>
  <el-row class="tac">
    <el-col :span="24">
      <div style="padding-left: 30px;color: rgb(255, 255, 255) !important;">
        <h4>书城后台管理</h4>
      </div>
      <el-menu default-active="1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
        <router-link to="myIndex">
          <el-menu-item index="1">
            <i class="el-icon-s-operation"></i>
            <span>主页</span>
          </el-menu-item>
        </router-link>
        <router-link to="manageUser">
          <el-menu-item index="/manageUser">
            <i class="el-icon-data-line"></i>
            <span>管理用户</span>
          </el-menu-item>
        </router-link>
        <router-link to="manageComment">
          <el-menu-item index="/manageComment">
            <i class="el-icon-data-line"></i>
            <span>管理评价</span>
          </el-menu-item>
        </router-link>
        <router-link to="manageBook">
          <el-menu-item index="/manageBook">
            <i class="el-icon-data-line"></i>
            <span>管理书籍</span>
          </el-menu-item>
        </router-link>
        <router-link to="manageOrder">
          <el-menu-item index="/manageOrder">
            <i class="el-icon-s-grid"></i>
            <span>管理订单</span>
          </el-menu-item>
        </router-link>
        <router-link to="myAbout">
          <el-menu-item index="/myAbout">
            <i class="el-icon-info"></i>
            <span>关于</span>
          </el-menu-item>
        </router-link>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "aside",
  data: function () {
    return {}
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  }
}
</script>

<style scoped>
a {
  text-decoration: none;
}

.router-link-active {
  text-decoration: none;
}

.el-menu-item-group__title {
  padding: 0px !important;
}

.el-menu-item.is-active {
  background-color: rgb(75, 223, 179) !important;
}

.el-menu-item:hover,
.el-submenu__title:hover {
  background-color: rgb(20, 27, 35);
}

.el-menu-item,
.el-submenu,
.el-submenu__title {
  color: white !important;
  background-color: rgb(34, 46, 60) !important;
}
</style>