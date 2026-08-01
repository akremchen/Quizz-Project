import { client } from './client.js'

export const authApi = {
    login: (payload) =>
        client.post('/users/login', payload).then((res) => res.data),

    register: (payload) =>
        client.post('/users/register', payload).then((res) => res.data),

    getMe: () =>
        client.get('/users/me').then((res) => res.data),

    updateProfile: (payload) =>
        client.put('/users/me', payload).then((res) => res.data),

    changePassword: (payload) =>
        client.patch('/users/me/password', payload),
}