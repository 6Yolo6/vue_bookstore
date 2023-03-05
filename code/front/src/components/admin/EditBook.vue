<template>
    <el-dialog :title="title" :visible="visible" :before-close="close">
        <el-form :model="form" :rules="rules" ref="form">
            <el-form-item label="名称" prop="name">
                <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="图片" :required="true">
                <el-upload ref="upload" class="avatar-uploader" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
                    :on-remove="handleRemove" :on-change="UploadImage" :before-upload="beforeUpload" :limit="1" :file-list="fileList" :auto-upload="false">
                    <img v-if="form.img" :src="form.img" class="img">
                    <div v-else style="width: 50px;">
                        <i class="el-icon-plus"></i>
                    </div>
                </el-upload>
            </el-form-item>
            <el-form-item label="作者" prop="valuauthore">
                <el-input v-model="form.author" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="类别" prop="category">
                <template slot>
                    <el-select style="float:left" v-model="form.category" placeholder="请选择" @change="change" clearable>
                        <el-option v-for="type, index in cate" :label="type.name" :value="type.id"
                            :key="index"></el-option>
                    </el-select>
                </template>
                <!-- <el-input v-model="form.category" autocomplete="off"></el-input> -->
            </el-form-item>
            <el-form-item label="出版社" prop="publisher">
                <el-input v-model="form.publisher" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="价格" prop="price">
                <el-input v-model="form.price" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="简介" prop="description" style="white-space: pre-wrap;">
                <el-input type="textarea" v-model="form.description" :maxlength="500"
                    :autosize="{ minRows: 1, maxRows: 5 }" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="close()">取 消</el-button>
            <el-button type="primary" @click="submit()">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
import api from '@/api';
import { Toast } from 'vant';

export default {
    name: "editBook",
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
            cate: [],
            saveOrUpdate: false,
            fileList: [],
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
    mounted() {
        let self = this;
        api.getAllCate("/category/getAllCate")
            .then(res => {
                self.cate = res.data
                if (JSON.stringify(self.defaultFormData) == "{}") {
                    //add
                    self.flag = false
                    self.saveOrUpdate = false
                } else {
                    //edit
                    self.form = JSON.parse(JSON.stringify(self.defaultFormData))
                    self.flag = true
                    self.saveOrUpdate = true
                }
            })
    },
    methods: {
        change() {
            console.log(this.form.category);
        },
        close() {
            this.flag = false
            //emit
            this.$emit("close", this.flag)
        },
        //上传图片的方法
        UploadImage(file, filelist) {
            let self = this;
            console.log(file);
            api.uploadImg("/book/uploadImg", file.raw)
                .then(res => {
                    console.log(res)
                    self.form.img = res
                })
        },
        //移除图片功能
        handleRemove(file, fileList) {
            console.log(file, fileList)
        },
        //预览图片功能
        handlePictureCardPreview(file) {
            console.log(file.url);
            // this.dialogVisible = true
            this.form.img = file.url
        },
        beforeUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        submit() {
            console.log(this.form);
            let self = this;
            if (!this.saveOrUpdate) {
                // console.log('新增');
                api.updateBook("/book/save", this.form)
                    .then(res => {
                        console.log(res.data);
                        self.flag = true
                        Toast("添加成功")
                        self.$emit("close", self.flag)
                    })
                    .catch(err => {
                        console.log(err)
                        self.flag = false
                        self.$emit("close", self.flag)
                    })
            }
            else {
                // console.log('更新');
                api.updateBook("/book/update", this.form)
                    .then(res => {
                        console.log(res.data);
                        self.flag = true
                        Toast("更新成功")
                        self.$emit("close", self.flag)
                    })
                    .catch(err => {
                        console.log(err)
                        self.flag = false
                        self.$emit("close", self.flag)
                    })
            }
        }
    }
}
</script>

<style scoped>
/* 图片上传css */
.avatar-uploader /deep/.el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    overflow: hidden;
    width: 90px;
    height: 90px;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;
}

.img {
    width: 80px;
    height: 80px;
    display: block;
}

</style>