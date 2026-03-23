import axios, { AxiosError } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

export type ApiResponse<T> = {
  code: number
  data: T
  message: string
}

export const http = axios.create({
  baseURL: '/',
  timeout: 20000,
})

http.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

http.interceptors.response.use(
  (resp) => {
    const body = resp.data as ApiResponse<unknown>
    if (body && typeof body.code === 'number') {
      if (body.code === 200) return body
      ElMessage.error(body.message || '请求失败')
      return Promise.reject(new Error(body.message || '请求失败'))
    }
    return resp.data
  },
  (err: AxiosError<any>) => {
    const status = err.response?.status
    const msg =
      err.response?.data?.message ||
      (status ? `网络错误（${status}）` : '网络错误（无法连接后端）')
    ElMessage.error(msg)
    if (status === 401) {
      const auth = useAuthStore()
      auth.clear()
    }
    return Promise.reject(err)
  },
)

