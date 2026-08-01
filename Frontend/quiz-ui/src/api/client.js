import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

export const AUTH_STORAGE_KEY = 'quiz-platform-auth'

export const client = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' },
})

client.interceptors.request.use((config) => {
    try {
        const session = JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY) || 'null')
        if (session?.token) config.headers.Authorization = `Bearer ${session.token}`
    } catch {
        localStorage.removeItem(AUTH_STORAGE_KEY)
    }
    return config
})

client.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401 && localStorage.getItem(AUTH_STORAGE_KEY)) {
            localStorage.removeItem(AUTH_STORAGE_KEY)
            window.dispatchEvent(new CustomEvent('quiz:session-expired'))
        }
        return Promise.reject(error)
    },
)

export function getApiError(error) {
    const data = error?.response?.data
    if (typeof data === 'string') return data
    if (data?.errors && typeof data.errors === 'object') {
        return Object.values(data.errors).flat().join(', ')
    }
    return data?.message || data?.error || error?.message || 'Something went wrong'
}
