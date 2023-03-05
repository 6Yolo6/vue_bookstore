import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/category',
    name: 'category',
    component: () => import('../components/mobile/Category.vue')
  },
  {
    path: '/detail',
    name: 'detail',
    component: () => import('../components/mobile/Detail.vue')
  },
  {
    path: '/cart',
    name: 'cart',
    component: () => import('../components/mobile/Cart.vue')
  },
  {
    path: '/frontUser',
    name: 'frontUser',
    component: () => import('../components/mobile/User.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../components/mobile/Login.vue')
  },
  {
    path: '/backLogin',
    name: 'backLogin',
    component: () => import('../components/admin/BackLogin.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../components/mobile/Register.vue')
  },
  {
    path: '/search',
    name: 'search',
    component: () => import('../components/mobile/Search.vue')
  },
  {
    path: '/order',
    name: 'order',
    component: () => import('../components/mobile/Order.vue')
  },
  {
    path: '/orderDetail',
    name: 'orderDetail',
    component: () => import('../components/mobile/OrderDetail.vue')
  },
  {
    path: '/setting',
    name: 'setting',
    component: () => import('../components/mobile/Setting.vue')
  },
  {
    path: '/favor',
    name: 'favor',
    component: () => import('../components/mobile/Favor.vue')
  },
  {
    path: '/confirmOrder',
    name: 'confirmOrder',
    component: () => import('../components/mobile/ConfirmOrder.vue')
  },
  {
    path: '/address',
    name: 'address',
    component: () => import('../components/mobile/Address.vue')
  },
  {
    path: '/comment',
    name: 'comment',
    component: () => import('../components/mobile/Comment.vue')
  },
  {
    path: '/myComment',
    name: 'myComment',
    component: () => import('../components/mobile/MyComment.vue')
  },
  {
    path: '/bookComment',
    name: 'bookComment',
    component: () => import('../components/mobile/BookComment.vue')
  },
  {
    path: '/reFund',
    name: 'reFund',
    component: () => import('../components/mobile/ReFund.vue')
  },
  {
    path: '/back',
    name: 'back',
    component: () => import('../components/admin/Back.vue'),
    children: [
      {
        path: '/myIndex',
        name: 'myIndex',
        component: () => import('../components/admin/MyIndex.vue')
      },
      {
        path: '/manageBook',
        name: 'manageBook',
        component: () => import('../components/admin/ManageBook.vue')
      }, 
      {
        path: '/manageUser',
        name: 'manageUser',
        component: () => import('../components/admin/ManageUser.vue')
      }, 
      {
        path: '/manageOrder',
        name: 'manageOrder',
        component: () => import('../components/admin/ManageOrder.vue')
      }, 
      {
        path: '/manageComment',
        name: 'manageComment',
        component: () => import('../components/admin/ManageComment.vue')
      },
      {
        path: '/myAbout',
        name: 'myAbout',
        component: () => import('../components/admin/MyAbout.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});
//配置路由拦截器
// router.beforeEach((to, from, next) => {
//     if (to.path === '/login' || to.path === '/register') {
//         next()
//     } else {
//         const token = localStorage.getItem('Authorization')
//         if (to.path === '/cart' && token == null) {
//             Toast("请登录")
//             next('/login')
//         }
//         next()
//     }
// })




export default router
