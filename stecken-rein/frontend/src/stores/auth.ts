import { defineStore } from 'pinia'
import api from '../services/api'

type User = {
  id: number
  firstName: string
  lastName: string
  email: string
  role: string
  approved: boolean
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: JSON.parse(localStorage.getItem('currentUser') || 'null') as User | null,
  }),

  getters: {
    isLoggedIn: (state) => state.currentUser !== null,
  },

  actions: {
      async login(email: string, password: string) {
      const response = await api.post('/auth/login', {
        email,
        password,
      })

      localStorage.setItem('token', response.data.token)

      localStorage.setItem(
        'currentUser',
        JSON.stringify(response.data)
      )

      this.currentUser = response.data
    },

    logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('currentUser')

        this.currentUser = null
    }
  },
})