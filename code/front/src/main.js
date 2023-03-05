import Vue from 'vue'
import './plugins/axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import store from './store'
import moment from "moment"
Vue.prototype.$moment = moment
Vue.use(ElementUI);

import { Icon } from 'vant';
import { Tabbar, TabbarItem } from 'vant';
import { Search } from 'vant';
import { List } from 'vant';
import { NavBar } from 'vant';
import { Card } from 'vant';
import { Tag } from 'vant';
import { Button } from 'vant';
import { Swipe, SwipeItem } from 'vant';
import { Lazyload } from 'vant';
import { GoodsAction, GoodsActionIcon, GoodsActionButton } from 'vant';
import { Grid, GridItem } from 'vant';
import { Image as VanImage } from 'vant';
import { SubmitBar } from 'vant';
import { Checkbox, CheckboxGroup } from 'vant';
import { Stepper } from 'vant';
import { Field } from 'vant';
import { NoticeBar } from 'vant';
import { Dialog } from 'vant';
import { Toast } from 'vant';
import { DropdownMenu, DropdownItem } from 'vant';
import { Col, Row } from 'vant';
import { Cell, CellGroup } from 'vant';
import { Tab, Tabs } from 'vant';
import { ContactCard } from 'vant';
import { AddressList } from 'vant';
import { AddressEdit } from 'vant';
import { ActionSheet } from 'vant';
import { Loading } from 'vant';
import { Rate } from 'vant';
import { CountDown } from 'vant';

Vue.use(CountDown);
Vue.use(Rate);
Vue.use(Loading);
Vue.use(ActionSheet);
Vue.use(AddressEdit);
Vue.use(AddressList);
Vue.use(ContactCard);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(CellGroup);
Vue.use(Col);
Vue.use(Row);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Toast);
Vue.use(Dialog);
Vue.use(NoticeBar);
Vue.use(Field);
Vue.use(Stepper);
Vue.use(Checkbox);
Vue.use(CheckboxGroup);
Vue.use(SubmitBar);
Vue.use(VanImage);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(GoodsAction);
Vue.use(GoodsActionButton);
Vue.use(GoodsActionIcon);
Vue.use(Lazyload);
Vue.use(Swipe);
Vue.use(SwipeItem);
Vue.use(Button);
Vue.use(Tag);
Vue.use(Card);
Vue.use(NavBar);
Vue.use(Cell);
Vue.use(List);
Vue.use(Search);
Vue.use(Tabbar);
Vue.use(TabbarItem);
Vue.use(Icon);



import axios from "axios";
Vue.prototype.$ajax = axios;

Vue.config.productionTip = false

//添加请求拦截器
axios.interceptors.request.use(
  config => {
    if (localStorage.getItem('Authorization')) {
      config.headers.Authorization = localStorage.getItem('Authorization');
    }
    return config;
  },
  error => {
    return Promise.reject(error);
});
//响应拦截器
axios.interceptors.response.use((success) => {
   if (success.status && success.status == 200) {}
   return success.data;
}, (error) => {
  // console.log(error.response);
   if (error.response.status == 504 || error.response.status == 404) {
      Toast("服务器被吃了")
   } else if (error.response.status == 414) {
    if (localStorage.getItem('Authorization')) {
      Toast("token过期，请登录")
      router.replace('/login')
    }
   } else {
      if (error.response.message) {
         Toast(error.response.message)
      }
   }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
