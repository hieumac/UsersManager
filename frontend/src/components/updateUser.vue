
<template>
  <div class="container">
    <div class="row">
      <div class="col-lg-6 col-md-6 col-sm-6 container justify-content-center card">
        <h1 class="text-center">edit User</h1>
        <div class="card-body">
          <form @submit="updateUser">
            <div class="form-group">
              <label>Full name: </label>
              <input type="text" v-model="user.fullname" name="fullname" class="form-control">
            </div>
            <div class="form-group">
              <label>Email: </label>
              <input type="text" v-model="user.email" name="email" class="form-control">
            </div>
            <div class="form-group">
              <label>Password: </label>
              <input type="password" v-model="user.password" name="password" class="form-control">
            </div>
            <br>
            <div class="box-footer">
              <button type="submit" class="btn btn-primary">Save</button>

            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {userService} from '../service/UserService'

export default {
  name: 'updateUser',
  props: ['id'],
  data () {
    return {
      user: {
        fullname: '',
        email: '',
        password: ''
      }
    }
  },
  created () {
    this.getUserById(this.$route.params.id)
  },
  methods: {
    async getUserById (id) {
      await userService.getUserById(id).then(res => {
        const userData = res.data
        this.user = {
          fullname: userData.fullname,
          email: userData.email,
          password: ''
        }
      })
    },
    async updateUser () {
      // Hiển thị hộp thoại xác nhận
      const confirmed = confirm('Bạn có chắc chắn muốn cập nhật với thông tin đã nhập?')
      if (!confirmed) {
        return
      }
      await userService.updateUser(this.$route.params.id, this.user).then(res => {
        if (res.HttpStatus === 'OK') {
          alert('Cập nhật User thành công')
          this.$router.push('/home')
        } else {
          alert('Cập nhật thất bại. ' + res.message)
        }
      })
    }
  }
}

</script>
