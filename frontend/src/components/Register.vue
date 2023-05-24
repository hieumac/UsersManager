<template>
    <div class="card" align="left">
         <div class="card-header">Register Form</div>
         <div class="card-body">

             <form  @submit.prevent="saveData">

             <label>User Name</label>
             <input type="text" v-model="user.fullname" name="fullname" id="fullname" class ="form-control"/>

             <label>User Email</label>
             <input type="email" v-model="user.email" name="email" id="email" class ="form-control"/>

             <label>User Password</label>
             <input type="password" v-model="user.password" name="password" id="password" class ="form-control"/>

             <input type="submit" value="Save" class="btn btn-success">
             </form>
         </div>
     </div>
</template>

<script>
import router from '../router'
import { authService } from '../service/LoginAndRegister'

export default {
  name: 'Register',
  data () {
    return {
      user: {
        fullname: '',
        email: '',
        password: ''
      }
    }
  },
  methods: {
    async saveData () {
      // Kiểm tra xem email, password và fullname có trống không
      if (!this.user.email || !this.user.password || !this.user.fullname) {
        alert('Vui lòng nhập đầy đủ thông tin')
        return
      }
      // Hiển thị hộp thoại xác nhận
      const confirmed = confirm('Bạn có chắc chắn muốn thêm mới với thông tin đã nhập?')
      if (!confirmed) {
        return
      }
      await authService.register(this.user).then(res => {
        if (res.HttpStatus === 'CREATED') {
          alert('Đăng ký thành công')
          this.$router.push('/')
        } else {
          alert('Đăng ký thất bại: ' + res.message)
        }
      })
    }
  },
  components: { router }
}
</script>
