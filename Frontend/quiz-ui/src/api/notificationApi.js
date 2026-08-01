import { client } from './client.js'

const asList = (data) =>
    Array.isArray(data)
        ? data
        : data?.notifications || data?.content || []

export const notificationApi = {
    getAll: () =>
        client.get('/notifications').then((res) => asList(res.data)),

    getUnread: () =>
        client.get('/notifications/unread').then((res) => asList(res.data)),

    markRead: (id) =>
        client.patch(`/notifications/${id}/read`).then((res) => res.data),
}