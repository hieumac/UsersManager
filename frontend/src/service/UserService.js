import { requestOptions } from '../_helpers/request'
import { handleResponse } from '../_helpers/response'

function getUsers () {
  return fetch('/api/v1/users/listUsers', requestOptions.get()).then(handleResponse)
}
function getUserById (id) {
  return fetch(`/api/v1/users/idUpdate/${id}`, requestOptions.get()).then(handleResponse)
}
function addNewUser (data) {
  return fetch('/api/v1/users/addNewUser', requestOptions.post(data)).then(handleResponse)
}
function updateUser (id, data) {
  return fetch(`/api/v1/users/update/${id}`, requestOptions.put(data)).then(handleResponse)
}
function deleteUser (id) {
  return fetch(`/api/v1/users/delete/${id}`, requestOptions.delete()).then(handleResponse)
}

export const userService = {
  getUsers,
  addNewUser,
  updateUser,
  deleteUser,
  getUserById
}
