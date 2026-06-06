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
      const response = await api.post('/auth/login', { email, password })
      this.currentUser = response.data
      localStorage.setItem('currentUser', JSON.stringify(response.data))
    },

    logout() {
      this.currentUser = null
      localStorage.removeItem('currentUser')
    },
  },
})