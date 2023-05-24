
import { requestNonToken } from '../_helpers/requestNonToken'
import { handleResponse } from '../_helpers/response'

function login (body) {
  return fetch('/api/v1/auth/login', requestNonToken.post(body)).then(handleResponse)
}
function register (body) {
  return fetch('/api/v1/auth/register', requestNonToken.post(body)).then(handleResponse)
}
export const authService = {
  login,
  register
}
