<template>
    <div class="login">
        <div class="content">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model.number="ruleForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                    <el-input type="password" @keyup.enter.native="submitForm('ruleForm')" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
            <div style="margin-top:20px">
                <router-link to="/">权限不足？点击去浏览前台</router-link>
            </div>
        </div>
    </div>
</template>
<script>
import api from '@/api'
import { Toast } from 'vant';

export default {
  name: 'backLogin',
  data() {
      var checkUsername = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('用户名不能为空'));
        }
        // setTimeout(() => {
        //   if (!Number.isInteger(value)) {
        //     callback(new Error('请输入数字值'));
        //   } else {
        //     if (value < 18) {
        //       callback(new Error('必须年满18岁'));
        //     } else {
        //       callback();
        //     }
        //   }
        // }, 1000);
        else
        callback()
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          password: '',
          checkPass: '',
          username: ''
        },
        rules: {
          password: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          username: [
            { validator: checkUsername, trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
            if (valid) {
                let self = this;
                var data = {
                    'username': this.ruleForm.username,
                    'password': this.ruleForm.password
                }
                api.login("/user/login", data)
                    .then(function (res) {
                        console.log(res);
                        if (res.statusCode == 200 && res.data.user.privilege == 1) {
                            Toast("登录成功");
                            self.$store.commit('setUser', {
                                Authorization: res.data.Authorization,
                                username: res.data.user.username,
                                nickname: res.data.user.nickname,
                            });
                            self.$router.push({ path: '/manageUser' });
                        }
                        else if (res.statusCode == 600) {
                            Toast(res.message)
                            if (res.message == "用户名不正确")
                              self.ruleForm.username = ""
                            else if (res.message == "密码错误") {
                              self.ruleForm.password = ""
                              self.ruleForm.checkPass = ""
                            }
                        }
                        else if (res.data.user.privilege == 0) {
                            Toast("权限不足，请联系管理员")
                        }
                    }).catch(function (err) {
                        console.log("error:", err);
                    })
            } else {
                Toast('error submit!!');
                return false;
            }
        });
        },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
}
</script>

<style scoped>
.login {
    background-color: white;
    padding: 15px;
}

.content {
    margin: 0 auto;
    width: 300px;
}

.van-button--large {
    width: 70%;
}
</style>