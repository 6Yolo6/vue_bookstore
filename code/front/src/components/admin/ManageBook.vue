<template>
    <div class="manageBook">
        <div style="margin-bottom: 20px;">
            <el-row justify="space-between" style="padding-left: 30px;">
                <el-col :span="6">
                    <el-select v-model="selected" placeholder="请选择" @change="change">
                        <el-option v-for="type, index in option" :label="type.text" :value="type.value"
                            :key="index"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="请输入" v-model="searchKey" clearable></el-input>
                </el-col>
                <el-col :span="6">
                    <el-button icon="el-icon-search" type="primary" @click="search">搜索</el-button>
                </el-col>
                <el-col :span="6">
                    <el-button type="primary" @click="add">新增</el-button>
                    <el-button type="primary" @click="delAll">批量删除</el-button>
                </el-col>
            </el-row>
        </div>

        <pageTable @load-table-data="loadTableData" ref="dataTable" @selection-change="selectionChange" row-key="id">
            <template slot="tb-columns">
                <el-table-column prop="name" label="名称" align="center" width="150"
                    :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="img" label="图片" align="center" width="100">
                    <template slot-scope="scope">
                        <img :src="scope.row.img" min-width="70" height="70" />
                    </template>
                </el-table-column>
                <el-table-column prop="author" label="作者" :show-overflow-tooltip="true" align="center" width="100"></el-table-column>
                <el-table-column prop="category" :filters="categorys" :filter-method="filterCategory" label="类别" align="center"
                    width="120">
                    <template slot-scope="scope">
                        {{scope.row.etc.cateName}}
                    </template>
                </el-table-column>
                <el-table-column prop="publisher" label="出版社" align="center" width="110"
                    :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="price" label="价格" align="center" width="80"></el-table-column>
                <el-table-column prop="description" label="简介" align="center"
                    :show-overflow-tooltip="true"></el-table-column>
                <el-table-column fixed="right" label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button @click="edit(scope.row)" type="text" size="small">编辑</el-button> ｜
                        <el-button @click="delOne(scope.row.id)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </template>
        </pageTable>

        <Edit :title="title" :visible="visible" v-if="visible" :defaultFormData="defaultFormData" @close=close>
        </Edit>
    </div>
</template>
<script>
import api from "@/api"
import Edit from "./EditBook"
import PageTable from './PageTable'
import { Toast } from 'vant'
export default {
    name: "manageBook",
    components: { PageTable, Edit },
    data() {
        return {
            title: "新增",
            visible: false,
            defaultFormData: {},
            ids: '',
            searchKey: '',
            type: 0,
            option: [
                { text: '书名', value: 1 },
                { text: '作者', value: 2 },
                { text: '出版社', value: 3 },
            ],
            categorys: [],
            selected: 1,
        }
    },
    created() {
        let self = this;
        api.getAllCate("/category/getAllCate")
            .then(res => {
                // console.log(res.data, "类别");
                let cate = []
                res.data.forEach(i => {
                    cate.push({ text: i.name, value: i.name})
                });
                self.categorys = cate
                console.log(self.categorys, "类别");
            })
    },
    methods: {
        filterCategory(value, row) {
            // console.log(row.etc.cateName, "row");
            // console.log(value, "value");
            return row.etc.cateName == value;
        },
        change(value) {
            console.log(value);
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
                api.deleteByIds("/book/deleteByIds", ids)
                    .then(res => {
                        //reload
                        console.log(res)
                        Toast("删除成功")
                        this.$refs.dataTable.loadTableData('reload', {})
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
        add() {
            this.visible = true
            this.defaultFormData = {}
        },
        edit(obj) {
            this.visible = true
            this.title = "编辑"
            this.defaultFormData = obj
        },
        loadTableData(filter, callback) {
            console.log(filter);
            if (this.searchKey != '') {
                api.searchBookByKey("/book/searchBookByKey", filter.pageNum, filter.pageSize, this.selected, this.searchKey)
                    .then(res => {
                        console.log(res);
                        let data = {
                            data: res.data.records,
                            total: res.data.total
                        }
                        //data
                        callback(data)
                    })
                    .catch(err => {
                        console.log(err);
                    })
            }
            else {
                api.getBookByCate("/book/getBookByCate", filter.pageNum, filter.pageSize, 0)
                    // page({
                    //     ...filter,
                    //     ...this.searchForm
                    // })
                    .then(res => {
                        console.log(res);
                        let data = {
                            data: res.data.records,
                            total: res.data.total
                        }
                        //data
                        callback(data)
                    })
                    .catch(err => {
                        console.log(err);
                    })
            }
        }
    }
}
</script>

<style scoped>
.manageBook {
    min-height: 1080px;
    margin: 20px
}

.el-tooltip__popper {
    /* 设置显示隐藏部分内容，按50%显示 */
    font-size: 14px;
    max-width: 50%;
}

.el-select-dropdown {
    min-width: 103.1px;
}
</style>