import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authApi } from '../api/authApi.js'
import { AUTH_STORAGE_KEY } from '../api/client.js'

const AuthContext = createContext(null)

function readSession() {
    try {
        return JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY) || 'null')
    } catch {
        localStorage.removeItem(AUTH_STORAGE_KEY)
        return null
    }
}

function normalizeLogin(data) {
    const token = data?.token || data?.accessToken || data?.jwt
    const user = data?.user || data?.userResponse || data
    if (!token || !user?.id) throw new Error('The login response did not contain a token and user.')
    return { token, user }
}

export function AuthProvider({ children }) {
    const [session, setSession] = useState(readSession)

    const logout = useCallback(() => {
        localStorage.removeItem(AUTH_STORAGE_KEY)
        setSession(null)
    }, [])

    useEffect(() => {
        const expired = () => logout()
        window.addEventListener('quiz:session-expired', expired)
        return () => window.removeEventListener('quiz:session-expired', expired)
    }, [logout])

    const login = useCallback(async (credentials) => {
        const next = normalizeLogin(await authApi.login(credentials))
        localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(next))
        setSession(next)
        return next.user
    }, [])

    const register = useCallback((payload) => authApi.register(payload), [])

    const refreshUser = useCallback(async () => {
        if (!session?.token) return null

        const user = await authApi.getMe()
        const next = { ...session, user }

        localStorage.setItem(
            AUTH_STORAGE_KEY,
            JSON.stringify(next)
        )

        setSession(next)

        return user
    }, [session])

    const updateProfile = useCallback(async (payload) => {
        const next = normalizeLogin(
            await authApi.updateProfile(payload)
        )

        localStorage.setItem(
            AUTH_STORAGE_KEY,
            JSON.stringify(next)
        )

        setSession(next)

        return next.user
    }, [])

    const value = useMemo(() => ({
        token: session?.token || null,
        user: session?.user || null,
        isAuthenticated: Boolean(session?.token && session?.user),
        isAdmin: String(session?.user?.role || '').toUpperCase() === 'ADMIN',
        login,
        register,
        logout,
        refreshUser,
        updateProfile,
    }), [
        session,
        login,
        register,
        logout,
        refreshUser,
        updateProfile,
    ])

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth must be used inside AuthProvider')
    return context
}
