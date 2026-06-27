<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'

type User = {
  id: number
  firstName: string
  lastName: string
  email: string
  role: string
  approved: boolean
}

const router = useRouter()
const authStore = useAuthStore()

const users = ref<User[]>([])

const loadUsers = async () => {
  const response = await api.get('/users')

  users.value = response.data.filter(
    (user: User) => user.id !== authStore.currentUser?.id
  )
}

const openChat = async (neighborId: number) => {
  await router.push(`/chat/${neighborId}`)
}

onMounted(loadUsers)
</script>

<template>
  <div>
    <h1>Private Chat</h1>

    <div v-if="users.length === 0" class="card">
      No neighbors available yet.
    </div>

    <div
      v-for="user in users"
      :key="user.id"
      class="user-card"
      @click="openChat(user.id)"
    >
      <strong>{{ user.firstName }} {{ user.lastName }}</strong>
      <p>{{ user.email }}</p>
      <small>Tap to open private chat</small>
    </div>
  </div>
</template>

<style scoped>
.user-card,
.card {
  background: white;
  padding: 16px;
  border-radius: 16px;
  margin-bottom: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.user-card {
  cursor: pointer;
}

.user-card p {
  margin: 6px 0;
  color: #555;
}

.user-card small {
  color: #777;
}
</style>