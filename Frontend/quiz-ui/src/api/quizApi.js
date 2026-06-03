import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const client = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' },
})

export const quizApi = {
    getAll: () => client.get('/quizzes').then((res) => res.data),
    getById: (id) => client.get(`/quizzes/${id}`).then((res) => res.data),
    getByCategory: (category) => client.get(`/quizzes/category/${encodeURIComponent(category)}`).then((res) => res.data),
    create: (payload) => client.post('/quizzes', payload).then((res) => res.data),
    update: (id, ownerId, payload) => client.put(`/quizzes/${id}`, payload, { params: { ownerId } }).then((res) => res.data),
    delete: (id, ownerId) => client.delete(`/quizzes/${id}`, { params: { ownerId } }).then((res) => res.data),
    publish: (id) => client.patch(`/quizzes/${id}/publish`).then((res) => res.data),
    submit: (id, payload) => client.post(`/quizzes/${id}/submit`, payload).then((res) => res.data),
    attemptsByUser: (userId) => client.get(`/quizzes/users/${userId}/attempts`).then((res) => res.data),
}

export function getApiError(error) {
    return error?.response?.data?.message || error?.response?.data?.error || error?.message || 'Something went wrong'
}
