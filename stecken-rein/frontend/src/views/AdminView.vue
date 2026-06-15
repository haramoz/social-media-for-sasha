<script setup lang="ts">
import { onMounted, ref } from 'vue'
import api from '../services/api'

type User = {
  id: number
  firstName: string
  lastName: string
  email: string
  role: string
  approved: boolean
}

const users = ref<User[]>([])
const message = ref('')

const loadPendingUsers = async () => {
  const response = await api.get('/admin/users/pending')
  users.value = response.data
}

const approveUser = async (id: number) => {
  await api.post(`/admin/users/${id}/approve`)
  message.value = 'User approved'
  await loadPendingUsers()
}

onMounted(loadPendingUsers)
</script>

<template>
  <div>
    <h1>Admin Approval</h1>

    <p>{{ message }}</p>

    <div v-if="users.length === 0">
      No pending users.
    </div>

    <div v-for="user in users" :key="user.id">
      <strong>{{ user.firstName }} {{ user.lastName }}</strong>
      <p>{{ user.email }}</p>

      <button @click="approveUser(user.id)">
        Approve
      </button>

      <hr />
    </div>
  </div>
</template>