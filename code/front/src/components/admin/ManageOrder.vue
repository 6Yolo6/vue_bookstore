<template>
    <el-main class="manageOrder">
        <div>
            <el-form ref="searchForm" :model="searchForm" :inline="true" class="formItem" label-width="80px">
          <el-col :xl="6" :md="8" :sm="24">
            <el-form-item label="用户名" prop="title" >
              <el-input  placeholder="请输入用户名" v-model="searchForm.name" clearable></el-input>
            </el-form-item>
          </el-col>

          <el-col :xl="6||24" :md="8||24" :sm="24">
            <el-form-item>
              <div :style=" { float: 'right', overflow: 'hidden' } || {} ">
                <el-button icon="el-icon-search" type="primary" @click="search">搜索</el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-form>
        </div>
        <div>
            <el-button type="primary" @click="add">新增</el-button>
            <el-button type="primary" @click="delAll">批量删除</el-button>
        </div>
        <pageTable @load-table-data="loadTableData" ref="dataTable" @selection-change="selectionChange" row-key="id">
            <template slot="tb-columns">
                <el-table-column prop="no" label="订单编号" align="center" width="300"></el-table-column>
                <el-table-column prop="username" label="用户" align="center" width="80">
                    <template slot-scope="scope">
                        {{ scope.row.etc.username }}
                    </template>
                </el-table-column>
                <el-table-column prop="address" label="收货地址" align="center" width="180" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        {{ scope.row.etc.address }}
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center" width="180"></el-table-column>
                <el-table-column prop="totalPrice" label="总价" align="center" width="80"></el-table-column>              
                <el-table-column prop="status" label="状态" align="center">
                    <template slot-scope="scope">
                        <div>
                            <div>{{ action(scope.row) }}</div>
                            <el-button style="cursor:pointer;" type="primary" @click="editDetail(scope.row)" size="small">管理</el-button>
                        </div>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="price" label="价格" align="center" width="80"></el-table-column>
                <el-table-column prop="description" label="简介" align="center" :show-overflow-tooltip="true"></el-table-column> -->
                <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button @click="edit(scope.row)" type="text" size="small">编辑</el-button> ｜
                        <el-button @click="delOne(scope.row.id)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </template>
        </pageTable>

        <EditDetail :title="title" :visible="visible" v-if="visible" :cancelReason="reason" :orderId="orderId" @close=close>
        </EditDetail>
        <EditOrder :title="title" :visible="visible1" v-if="visible1" :defaultFormData="defaultFormData" @close=close1>
        </EditOrder>
    </el-main>
</template>

<script>

import api from "@/api"
import EditOrder from "./EditOrder"
import EditDetail from "./EditOrderDetail"
import PageTable from './PageTable'
export default {
    name: "manageOrder",
    components: { PageTable, EditOrder, EditDetail },
    data() {
        return {
            title: "新增",
            visible: false,
            visible1: false,
            orderId: '',
            ids: '',
            searchForm: {
                name: '',
            },
            reason: '',
            defaultFormData: {},
        }
    },
    methods: {
        action(order) {
            let action = ['等待买家付款','待发货','等待买家确认收货','等待买家评价','退款/售后']
            if (order.status == -1)
                return '已评价'
            else if (order.status == -2) {
                if (order.cancelReason != "超时自动取消")
                    return '交易关闭'
                else
                    return '超时取消'
            }               
            else
                return action[order.status-1]
        },
        search() {
            this.$refs.dataTable.loadTableData('reload')
        },
        selectionChange(selection) {
            //selection表示选中的记录 array
            var aryIds = []
            for (var item of selection) {
                aryIds.push(item.id)
            }

            this.ids = aryIds.join(",")

        },
        delAll() {
            if (this.ids.length <= 0) {
                this.$message({
                    message: '请先选择记录',
                    type: 'warning'
                });

                return
            }
            this.del(this.ids)
        },
        delOne(id) {
            this.del(id)
        },
        del(ids) {
            this.$confirm('是否确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //点击确认的时候 触发
                let self = this;
                api.deleteByIds("/order/deleteByIds", ids)
                    .then(res => {
                        //reload
                        console.log(res);
                        self.$refs.dataTable.loadTableData('reload')
                    }).catch(err => {
                        console.log(err)
                    })
            }).catch(() => {
                //点击取消的时候触发         
            });
        },
        close(val) {
            this.visible = false
            if (val) {
                //reload
                this.$refs.dataTable.loadTableData('reload')
                //this.$refs.dataTable = document.getElementById('dataTable')
            }
        },
        close1(val) {
            this.visible1 = false
            if (val) {
                //reload
                this.$refs.dataTable.loadTableData('reload')
                //this.$refs.dataTable = document.getElementById('dataTable')
            }
        },
        add() {
            // this.visible1 = true
            // this.defaultFormData = {}
        },
        edit(obj) {
            this.visible1 = true
            this.title = "编辑"
            // console.log(obj);
            
            this.defaultFormData = obj
            
        },
        editDetail(order) {
            console.log(order)
            this.visible = true
            this.title = "订单详情"
            this.orderId = order.id
            this.reason = order.cancelReason
        },
        loadTableData(filter, callback) {

            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(function (res1) {
                    // console.log(res1.statusCode);
                    if (res1.statusCode == 200) {
                        api.getAll("/order/getAll", filter.pageNum, filter.pageSize)
                            .then(res => {
                                console.log(res.data, "分页订单");
                                let data = {
                                    data: res.data.records,
                                    total: res.data.total
                                }
                                callback(data)
                            })
                            .catch(err => {
                                console.log(err);
                            })
                    }
                    else if (res1.statusCode == 401) {
                        Toast("token过期");
                        self.$router.push('login');
                    }
                })
        }
    }
}
</script>


<style scoped>
.manageOrder {
    min-height: 1080px
}
</style>