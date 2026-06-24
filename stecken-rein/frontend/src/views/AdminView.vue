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

const rejectUser = async (id: number) => {

    if (!confirm("Reject this registration request?"))
        return

    await api.delete(`/admin/users/${id}`)

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

      <div class="actions">

        <button
            class="approve-button"
            @click="approveUser(user.id)"
        >
            ✓ Approve
        </button>

        <button
            class="reject-button"
            @click="rejectUser(user.id)"
        >
            ✕ Reject
        </button>

      </div>

      <hr />
    </div>
  </div>
</template>

<style scoped>
  .actions {
    display: flex;
    justify-content: flex-start;
    gap: 10px;
    margin-top: 16px;
  }

  .approve-button,
  .reject-button {
      width: auto;
      min-width: 110px;
      padding: 10px 18px;
      border: none;
      border-radius: 10px;
      cursor: pointer;
      font-weight: 600;
      transition: background-color 0.2s ease;
  }

  .approve-button {
      background: #22c55e;
      color: white;
  }

  .approve-button:hover {
      background: #16a34a;
  }

  .reject-button {
      background: #ef4444;
      color: white;
  }

  .reject-button:hover {
      background: #dc2626;
  }

</style>