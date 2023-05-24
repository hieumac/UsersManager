import Vue from 'vue'
import Router from 'vue-router'
import Register from '@/components/Register'
import Login from '@/components/Login'
import Home from '@/components/Home'
import addNew from '@/components/addNew'
import updateUser from '@/components/updateUser'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/addNew',
      name: 'addNew',
      component: addNew
    },
    {
      path: '/updateUser/:id',
      name: 'updateUser',
      component: updateUser,
      props: true
    }

  ]
})
