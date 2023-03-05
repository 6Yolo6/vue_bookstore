<template>
    <el-dialog :title="title" :visible="visible" :before-close="close">
        <el-form :model="form" :rules="rules" ref="form">
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.etc.nickname" autocomplete="off"></el-input>
                <!-- {{ nickname(form) }} -->
            </el-form-item>
            <el-form-item label="书名" prop="bookName">
                <el-input v-model="form.etc.bookName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="评分" prop="rate">
                <template slot>
                    <van-rate style="float: left;margin-top: 9px;" readonly v-model="form.rate" />
                </template>
            </el-form-item>
            <el-form-item label="评价" prop="content">
                <el-input v-model="form.content" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
import api from '@/api'
import { Toast } from 'vant'

export default {
    name: "editComment",
    props: {
        visible: {
            type: Boolean,
            default: false
        },
        title: {
            type: String,
            default: "新增"
        },
        defaultFormData: {
            type: Object
        }
    },
    data() {
        return {
            flag: false, //false = add  true = update
            form: {},
            flag: false,
            saveOrUpdate: false,
            rules: {
                name: [{ required: true, message: "不能为空" },
                    //  {type: "email", message: "邮箱格式不正确"}
                ],
                value: [{ required: true, message: "不能为空", trigger: "blur" },
                    // {validator: validateTelephone, trigger: "blur"}
                ]
            }
        }
    },
    created() {
        // this.nickname(this.defaultFormData)
        if (JSON.stringify(this.defaultFormData) == "{}") {
            //add
            this.flag = false
            this.saveOrUpdate = false
        } else {
            //edit
            this.form = JSON.parse(JSON.stringify(this.defaultFormData))
            console.log(this.form)
            this.flag = true
            this.saveOrUpdate = true
        }
    },
    methods: {
        
        close() {
            this.flag = false
            //emit
            this.$emit("close", this.flag)
        },
        submit() {
            //  this.$refs.form.validate(valid => {
            //     if (valid) {
            console.log(this.form);
           
            let self = this;
            self.flag = true
            self.$emit("close", self.flag)
            // api.updateUser("/user/update", {...this.form})
            //     .then(res => {
            //         console.log(res)
            //         self.flag = true
            //         self.$emit("close", self.flag)
            //         Toast("更新成功")
            //     })
            //     .catch(err => {
            //         console.log(err)
            //         self.flag = false
            //         self.$emit("close", self.flag)
            //     })
            //     }
            //  })
        }
    }
}
</script>

<style scoped>
.privilege {
    float:left;
}
</style>