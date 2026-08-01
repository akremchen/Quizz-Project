import { useState } from 'react'
import { Save, KeyRound } from 'lucide-react'
import { useAuth } from '../context/AuthContext.jsx'
import { authApi } from '../api/authApi.js'
import { getApiError } from '../api/client.js'

export default function Profile({ setToast }) {
    const { user, updateProfile } = useAuth()

    const [profile, setProfile] = useState({
        username: user.username || '',
        email: user.email || '',
    })

    const [passwords, setPasswords] = useState({
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
    })

    const [savingProfile, setSavingProfile] = useState(false)
    const [savingPassword, setSavingPassword] = useState(false)

    async function submitProfile(event) {
        event.preventDefault()

        if (!profile.username.trim() || !profile.email.trim()) {
            setToast({
                type: 'error',
                message: 'Username and email are required',
            })
            return
        }

        try {
            setSavingProfile(true)

            await updateProfile({
                username: profile.username.trim(),
                email: profile.email.trim(),
            })

            setToast({
                type: 'success',
                message: 'Profile updated successfully',
            })
        } catch (error) {
            setToast({
                type: 'error',
                message: getApiError(error),
            })
        } finally {
            setSavingProfile(false)
        }
    }

    async function submitPassword(event) {
        event.preventDefault()

        if (passwords.newPassword !== passwords.confirmPassword) {
            setToast({
                type: 'error',
                message: 'The new passwords do not match',
            })
            return
        }

        if (passwords.newPassword.length < 8) {
            setToast({
                type: 'error',
                message: 'The new password must contain at least 8 characters',
            })
            return
        }

        try {
            setSavingPassword(true)

            await authApi.changePassword({
                currentPassword: passwords.currentPassword,
                newPassword: passwords.newPassword,
            })

            setPasswords({
                currentPassword: '',
                newPassword: '',
                confirmPassword: '',
            })

            setToast({
                type: 'success',
                message: 'Password changed successfully',
            })
        } catch (error) {
            setToast({
                type: 'error',
                message: getApiError(error),
            })
        } finally {
            setSavingPassword(false)
        }
    }

    return (
        <section className="panel">
            <div className="page-heading">
                <div>
                    <span className="eyebrow">Account settings</span>
                    <h1>Edit profile</h1>
                    <p>Manage your account information and password.</p>
                </div>
            </div>

            <form className="quiz-form" onSubmit={submitProfile}>
                <h2>Personal information</h2>

                <label>
                    Username
                    <input
                        value={profile.username}
                        onChange={(event) =>
                            setProfile((current) => ({
                                ...current,
                                username: event.target.value,
                            }))
                        }
                    />
                </label>

                <label>
                    Email
                    <input
                        type="email"
                        value={profile.email}
                        onChange={(event) =>
                            setProfile((current) => ({
                                ...current,
                                email: event.target.value,
                            }))
                        }
                    />
                </label>

                <button className="btn primary" disabled={savingProfile}>
                    <Save size={18} />
                    {savingProfile ? 'Saving...' : 'Save profile'}
                </button>
            </form>

            <hr className="profile-divider" />

            <form className="quiz-form" onSubmit={submitPassword}>
                <h2>Change password</h2>

                <label>
                    Current password
                    <input
                        type="password"
                        value={passwords.currentPassword}
                        onChange={(event) =>
                            setPasswords((current) => ({
                                ...current,
                                currentPassword: event.target.value,
                            }))
                        }
                    />
                </label>

                <label>
                    New password
                    <input
                        type="password"
                        value={passwords.newPassword}
                        onChange={(event) =>
                            setPasswords((current) => ({
                                ...current,
                                newPassword: event.target.value,
                            }))
                        }
                    />
                </label>

                <label>
                    Confirm new password
                    <input
                        type="password"
                        value={passwords.confirmPassword}
                        onChange={(event) =>
                            setPasswords((current) => ({
                                ...current,
                                confirmPassword: event.target.value,
                            }))
                        }
                    />
                </label>

                <button className="btn primary" disabled={savingPassword}>
                    <KeyRound size={18} />
                    {savingPassword ? 'Changing...' : 'Change password'}
                </button>
            </form>
        </section>
    )
}