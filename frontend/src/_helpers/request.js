export const requestOptions = {
  get () {
    return {
      method: 'GET',
      ...headers()
    }
  },
  post (body) {
    return {
      method: 'POST',
      body: JSON.stringify(body),
      ...headers()
    }
  },
  patch (body) {
    return {
      method: 'PATCH',
      ...headers(),
      body: JSON.stringify(body)
    }
  },
  put (body) {
    return {
      method: 'PUT',
      ...headers(),
      body: JSON.stringify(body)
    }
  },
  delete () {
    ;
    return {
      method: 'DELETE',
      ...headers()
    }
  }
}

function headers () {
  const token = localStorage.getItem('jwtToken') || ''
  const authHeader = token
    ? { Authorization: 'Bearer ' + token }
    : {}
  return {
    headers: {
      ...authHeader,
      'Content-Type': 'application/json'
    }
  }
}
