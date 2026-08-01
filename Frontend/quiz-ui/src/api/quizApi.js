import { client, getApiError } from './client.js'

export const quizApi = {
    getAll: () => client.get('/quizzes').then((res) => res.data),
    getById: (id) => client.get(`/quizzes/${id}`).then((res) => res.data),
    getByCategory: (category) => client.get(`/quizzes/category/${encodeURIComponent(category)}`).then((res) => res.data),
    create: (payload) => client.post('/quizzes', payload).then((res) => res.data),
    update: (id, payload) => client.put(`/quizzes/${id}`, payload).then((res) => res.data),
    delete: (id) => client.delete(`/quizzes/${id}`).then((res) => res.data),
    publish: (id) => client.patch(`/quizzes/${id}/publish`).then((res) => res.data),
    submit: (id, payload) => client.post(`/quizzes/${id}/submit`, payload).then((res) => res.data),
        attemptsByUser: () =>
            client.get('/quizzes/attempts').then((res) => res.data),
    unlock: (id) => client.post(`/quizzes/${id}/unlock`).then((res) => res.data),
}

export { getApiError }
