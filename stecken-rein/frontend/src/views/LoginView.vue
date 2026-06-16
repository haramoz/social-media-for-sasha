<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const message = ref('')

const login = async () => {
  try {
    await authStore.login(email.value, password.value)
    await router.push('/feed')
    } catch (error: any) {
    console.error(error)
    message.value =
      error.response?.data?.message ||
      error.response?.data ||
      'Login failed'
  }
}
</script>

<template>
  <div class="card">
    <h1>Login</h1>

    <input v-model="email" placeholder="Email" />
    <input v-model="password" placeholder="Password" type="password" />

    <button @click="login">Login</button>

    <p>{{ message }}</p>
  </div>
</template>

<style scoped>
.card {
  background: white;
  padding: 20px;
  border-radius: 16px;
  max-width: 420px;
  margin: 0 auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

input,
button {
  width: 100%;
  box-sizing: border-box;
  padding: 12px;
  margin-top: 12px;
  font-size: 16px;
  border-radius: 10px;
}

input {
  border: 1px solid #ccc;
}

button {
  border: none;
  background: #2563eb;
  color: white;
  font-weight: bold;
}
</style>