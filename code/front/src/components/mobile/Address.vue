<template>
    <div class="address">
        <van-nav-bar left-text="我的收货地址" right-text="管理" left-arrow @click-left=$router.go(-1)>

        </van-nav-bar>
        <van-action-sheet v-model="show" close-on-click-action>
            <template slot="default">
                <van-address-edit :address-info="list[0]" :area-list="areaList" show-delete show-search-result
                    :search-result="searchResult" :area-columns-placeholder="['请选择', '请选择', '请选择']" @save="onSave"
                    @delete="onDelete" @change-detail="onChangeDetail" />
            </template>
        </van-action-sheet>
        <van-address-list v-model="chosenAddressId" :list="list" default-tag-text="默认"
             @edit="onEdit">

        </van-address-list>

    </div>
</template>

<script>
import api from "@/api"
import { areaList } from '@vant/area-data';
import { Toast } from 'vant';

export default {
    name: 'address',
    data: function () {
        return {
            chosenAddressId: '1',
            list: [],
            show: false,
            areaList: areaList,
            searchResult: [],
        }
    },
    created: function() {
        this.getUser()
    },
    methods: {
        onEdit() {
            this.show = true
        },
        onSave(myAddress) {
            console.log(myAddress);
            let address = myAddress.province + myAddress.city + myAddress.county + " " + myAddress.addressDetail;
            
            var data = {
                'telephone': myAddress.tel,
                'address': address
            }
            let self = this;
            api.updateUser("/user/updateUser", data)
                .then(res => {
                    console.log(res.data);
                    self.user = res.data.user;
                    self.show = false;
                    self.getUser();
                })
        },
        onDelete() {
            Toast("删除成功");
        },
        onChangeDetail(val) {
            console.log(val);
        },
        getUser() {
            let self = this;
            api.getUser("/user/getUser", localStorage.getItem('username'))
                .then(res => {
                    console.log(res.data);
                    self.user = res.data.user;
                    self.list = [];
                    self.list.push({ name: self.user.username, tel: self.user.telephone, address: self.user.address, isDefault: true, });
                })
        }
    }
}
</script>

<style scoped>
.content {
    padding: 16px 16px 160px;

}
.van-address-edit__buttons {
    padding: 0px;
}
</style>