export const requestNonToken = {

  post (body) {
    return {
      method: 'POST',
      body: JSON.stringify(body),
      ...headers()
    }
  }
}
function headers () {
  return {
    headers: {
      'Content-Type': 'application/json'
    }
  }
}
