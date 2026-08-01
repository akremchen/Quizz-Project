import { client, getApiError } from './client.js'

export const achievementApi = {
    ping: () =>
        client.get('/achievement/ping').then((res) => res.data),

    getPoints: () =>
        client.get('/achievement/points').then((res) => res.data),

    getBadges: () =>
        client.get('/achievement/badges').then((res) => res.data),
}

export function getAchievementApiError(error) {
    return getApiError(error)
}