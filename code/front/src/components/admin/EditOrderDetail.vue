<template>
    <el-dialog :title="title" :visible="visible" :before-close="close">
        <el-table :data="form">
                <el-table-column prop="name" label="书名" :show-overflow-tooltip="true" align="center" width="200"></el-table-column>
                <el-table-column prop="img" label="图片" align="center" width="100">
                    <template slot-scope="scope">            
                        <img :src="scope.row.img"  min-width="70" height="70" />
                    </template>   
                </el-table-column>
                <el-table-column prop="num" label="数量" align="center" width="60"></el-table-column>
                <el-table-column prop="price" label="价格" align="center" width="70"></el-table-column>
                <el-table-column prop="status" :label="cancelReason == null ? '状态' : '取消原因'" align="center" width="180">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.status == 2" @click="updateDetail(scope.row)" type="success">去发货</el-button>
                        <div v-else >
                            <div v-if="scope.row.status == -2">
                                {{ cancelReason }}
                            </div>
                            <div v-else>
                                {{ action(scope.row.status) }}
                            </div>
                        </div>
                    </template>
                </el-table-column>
                 <!-- <el-table-column prop="price" label="价格" align="center" width="80"></el-table-column> -->
                <!-- <el-table-column prop="description" label="简介" align="center" :show-overflow-tooltip="true"></el-table-column> -->
                <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small">编辑</el-button> ｜
                        <el-button @click="delOne(scope.row)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

        <!-- <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </div> -->
    </el-dialog>
</template>

<script>
import api from '@/api';
import { Toast } from "vant";

export default {
    name: "editOrderDetail",
    props: {
        visible: {
            type: Boolean,
            default: false
        },
        title: {
            type: String,
            default: "新增"
        },
        orderId: {
            type: Number
        },
        cancelReason: {
            type: String
        }
    },
    data() {
        return {
            flag: false, //false = add  true = update
            form: [],
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
    mounted() {
        
        if (this.orderId != '') {
            this.getDetail()
        }
        // if (JSON.stringify(this.defaultFormData) == "{}") {
        //     //add
        //     this.flag = false
        //     this.saveOrUpdate = false
        // } else {
        //     //edit
        //     this.form = JSON.parse(JSON.stringify(this.defaultFormData))

        //     this.flag = true
        //     this.saveOrUpdate = true
        // }
    },
    methods: {
        action(status) {
            let action = ['等待买家付款','去发货','等待买家确认收货','等待买家评价','退款/售后']
            if (status == -1)
                return '已评价'
            else if (status == -2)
                return '交易关闭'
            else
                return action[status-1]
        },
        getDetail() {
            let self = this;
            api.getDetailByOrderId("/orderDetail/getByOrderId", this.orderId)
                .then(res => {
                    console.log(res.data);
                    let books = []
                    let carts = []
                    carts = res.data

                    let bookIds = []
                    carts.forEach(i => {
                        bookIds.push(i.bookId)

                    })
                    // console.log(bookIds.join(","));
                    api.getCartBookByIds("/book/getBookByIds", bookIds.join(","))
                        .then(function (res2) {
                            //合并数组
                            console.log(res2.data)
                            books = carts.map((item, index) => {
                                return { ...res2.data[index], ...item };
                            })
                            // console.log(books)
                            self.form = books
                            console.log(self.form)
                            self.flag = true
                            self.saveOrUpdate = true
                        })
                })
        },
        updateDetail(book) {
            // console.log(book)
            let self = this
            api.updateOrderDetail("/orderDetail/update", book.orderId, book.bookId, book.status)
                .then(res => {
                    console.log(res.data)
                    Toast("更新成功")
                    self.getDetail()
                })
        },
        edit() {

        },
        close() {
            this.flag = false
            //emit
            this.$emit("close", this.flag)
        },
    }
}
</script>

<style scoped>

.el-tooltip__popper { 
    font-size: 14px;
    max-width: 50%;
}
</style>