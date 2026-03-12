import axios from 'axios'

export const api = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.response.use(
  (res) => res.data,
  (err) => {
    const msg = err.response?.data?.message || err.message || '请求失败'
    console.error('[API Error]', msg)
    return Promise.reject(new Error(msg))
  }
)
