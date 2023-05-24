<template>
  <div class="container">
    <div class="d-flex bd-highlight mb-3">
        <div class="me-auto p-2 bd-highlight"><h2>User List</h2></div>
        <div class="p-2 bd-highlight" style="display: flex;">
          <button class="btn btn-primary" style="margin-right: 10px" @click="refreshUserList">Refresh</button>
          <router-link to="/addNew" class="btn btn-primary">Create</router-link>
          <router-link to="/" class="btn btn-danger" style="margin-left: 10px" @click="logout">Logout</router-link>
        </div>
      </div>
      <div class="mb-3 form-inline">
        <div class="input-group">
          <input ref="searchInput" type="text" class="form-control mr-2" v-model="keyword" placeholder="Enter name..." />
          <button class="btn btn-primary" @click="findUser">Search</button>
        </div>
      </div>

    <!-- <h1 class="text-center">Employees List</h1> -->
    <table class="table">
        <thead>
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Full Name</th>
          <th scope="col">Email</th>
          <th scope="col">Is Active</th>
        </tr>
        </thead>
        <tbody class="table-group-divider">
          <tr v-for="user in filteredUsers" v-bind:key="user.id">
            <td>{{user.id}}</td>
            <td>{{user.fullname}}</td>
            <td>{{user.email}}</td>
            <td>{{user.enabled}}</td>
            <td>
              <router-link :to="`/updateUser/${user.id}`" class="btn btn-secondary">Edit</router-link>
              <a  @click="deleteUser(user.id)" class="btn btn-danger">Delete</a>
            </td>
          </tr>
        </tbody>
      </table>
  </div>
</template>

<script>
import { userService } from '../service/UserService'

export default {
  name: 'Home',
  data () {
    return {
      originalUsers: [],
      filteredUsers: [],
      keyword: ''
    }
  },
  methods: {
    async getUsers () {
      await userService.getUsers().then(res => {
        if (res.HttpStatus === 'OK') {
          this.originalUsers = res.data
          this.filteredUsers = res.data
        } else {
          alert('Hiện danh sách Users thất bại. ' + res.message)
        }
      })
    },

    async deleteUser (id) {
      const confirmed = confirm('Bạn có chắc chắn muốn xóa?')
      if (confirmed) {
        await userService.deleteUser(id).then(res => {
          if (res.HttpStatus === 'OK') {
            alert('Đã xóa thành công')
            this.getUsers()
          } else {
            alert('Xóa thất bại. ' + res.message)
          }
        })
      }
    },
    logout () {
      window.localStorage.clear()
    },
    async findUser () {
      this.filteredUsers = this.originalUsers.filter((user) =>
        user.fullname.toLowerCase().includes(this.keyword.toLowerCase())
      )
      if (this.filteredUsers.length === 0) {
        alert('Không tìm thấy người dùng, hãy nhập lại')
        this.filteredUsers = this.originalUsers
        this.$refs.searchInput.focus()
      }
    },
    async refreshUserList () {
      this.keyword = '' // Reset giá trị keyword về rỗng
      this.getUsers()
    }
  },
  created () {
    this.getUsers()
  }
}
</script>
