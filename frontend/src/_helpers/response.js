
export function handleResponse (response) {
  return response.json().then(data => {
    // if (!response.ok) {
    //   if ([401, 403].indexOf(response.status) !== -1) {
    //     window.location.href = '/login'
    //   }
    //   const error = (data && data.message) || response.statusText
    //   return Promise.reject(error)
    // }
    return data
  })
}
