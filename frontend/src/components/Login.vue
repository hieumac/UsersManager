<template>

    <div class="row">

    <div class="col-sm-4" >
     <h2 align="center"> Login</h2>

     <form @submit.prevent="LoginData">

     <div class="form-group" align="left">
       <label>Email</label>
       <input type="email" v-model="user.email" class="form-control"  placeholder="Email">
     </div>

    <div class="form-group" align="left">
    <label>Password</label>
    <input type="password" v-model="user.password" class="form-control"  placeholder="Password">
  </div>
  <br/>

     <button type="submit" class="btn btn-primary">Login</button>
    <div class="btn btn-primary"><router-link to="/register" class="nav-link">Sign Up</router-link></div>
     </form>
   </div>
   </div>

</template>

<script>
import { authService } from '../service/LoginAndRegister'

export default {
  name: 'Login',
  data () {
    return {
      user: {
        email: '',
        password: ''
      }
    }
  },
  methods: {
    async LoginData () {
      // Kiểm tra xem email và password có trống không
      if (!this.user.email || !this.user.password) {
        alert('Vui lòng nhập đầy đủ email và password')
        return
      }
      await authService.login(this.user).then(res => {
        if (res.HttpStatus === 'ACCEPTED') {
          window.localStorage.clear()
          window.localStorage.setItem('jwtToken', res.data)
          this.$router.push('/home')
        } else {
          alert('Đăng nhập thấy bại. ' + res.message)
        }
      })
    }
  }
}
</script>
