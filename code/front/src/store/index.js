import { Toast } from 'vant'
import { Dialog } from 'vant';

import Vue from 'vue'
import Vuex from 'vuex'
import api from '@/api'
import router from '@/router'
import store from '@/store'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    bookList: [],
    cartNum: '',
    favorNum: '',
    commentNum: '',
    bookCommentNum: '',
    orderNum: [],
    allCheck: false,
    allPrice: 0,
    // user: {
    //   Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization'): '',
    //   username: localStorage.getItem('username') ? localStorage.getItem('username'): ''
    // }
  },
  getters: {
    getFavorNum(state) {
      return state.favorNum;
    },
    getCommentNum(state) {
      return state.commentNum;
    },
    getbookCommentNum(state) {
      return state.bookCommentNum;
    },
    getCartNum(state) {
      return state.cartNum;
    },
    getOrderNum(state) {
      return function(status) {
        return state.orderNum[status];
      }
    },
  },
  mutations: {
    setUser(state, user) {
      localStorage.setItem('username', user.username);
      localStorage.setItem('nickname', user.nickname);
      localStorage.setItem('Authorization', user.Authorization);
      // state.user.Authorization = user.Authorization;
      // state.user.username = user.username;
    },
    setCartNum(state, cartNum) {
      state.cartNum = cartNum
    },
    setFavorNum(state, favorNum) {
      state.favorNum = favorNum
    },
    setCommentNum(state, num) {
      state.commentNum = num;
    },
    setbookCommentNum(state, num) {
      state.bookCommentNum = num;
    },
    setOrderNum(state, orderNum) {
      state.orderNum = orderNum;
    }
  },
  actions: {
    setCartNum(context) {
      api.getByUserId("/cart/getByUserId")
        .then(res => {
          let cartNum;
          if (res.data)
            cartNum = res.data.length
          else
            cartNum = ''
          context.commit('setCartNum', cartNum)
        })
    },
    setFavorNum(context) {
      api.getByUserId("/favor/getByUserId")
        .then(res => {
          let favorNum;
          if (res.data)
            favorNum = res.data.length;
          else
            favorNum = '';
          context.commit('setFavorNum', favorNum)
        })
    }
  },
  modules: {

  }
})
