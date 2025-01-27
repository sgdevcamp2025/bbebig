import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8083'
})

export default axiosInstance
