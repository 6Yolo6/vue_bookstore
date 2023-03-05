<template>
    <el-dialog :title="title" :visible="visible" :before-close="close">
        <el-form :model="form" :rules="rules" ref="form">
            <el-form-item label="订单编号" prop="no">
                <el-input v-model="form.no" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
                <el-input disabled v-model="form.etc.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="收货地址" prop="address">
                <el-input v-model="form.etc.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="创建时间" prop="cerateTime">
                <el-input v-model="form.createTime" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="总价" prop="totalPrice">
                <el-input v-model="form.totalPrice" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-input v-model="form.status" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>


export default {
    name: "editOrder",
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
        if (JSON.stringify(this.defaultFormData) == "{}") {
            //add
            this.flag = false
            this.saveOrUpdate = false
        } else {
            //edit
            this.form = JSON.parse(JSON.stringify(this.defaultFormData))
            console.log(this.form);
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
            this.flag = true
            this.$emit("close", this.flag)
            // save({
            //     ...this.form
            // }).then(res => {
                
            // })
            //     .catch(err => {
            //         console.log(err)
            //         this.flag = false
            //         this.$emit("close", this.flag)
            //     })
            //     }
            //  })
        }
    }
}
</script>

<style scoped>

</style>