<script setup lang="ts">
import { ref } from 'vue'
import api from '../services/api'

const email = ref('')
const password = ref('')
const message = ref('')

const login = async () => {
  try {
    const response = await api.post('/auth/login', {
      email: email.value,
      password: password.value,
    })

    localStorage.setItem('currentUser', JSON.stringify(response.data))

    message.value = `Welcome, ${response.data.firstName}!`
  } catch (error) {
    message.value = 'Login failed or account not approved yet.'
  }
}
</script>

<template>
  <div>
    <h1>Login</h1>

    <input v-model="email" placeholder="Email" />
    <br /><br />

    <input v-model="password" placeholder="Password" type="password" />
    <br /><br />

    <button @click="login">Login</button>

    <p>{{ message }}</p>
  </div>
</template>