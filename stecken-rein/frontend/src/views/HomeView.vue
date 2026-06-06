<script setup lang="ts">
import { onMounted, ref } from 'vue'
import api from '../services/api'

const backendStatus = ref('Checking backend...')

onMounted(async () => {
  const response = await api.get('/health')
  backendStatus.value = response.data
})
</script>

<template>
  <div>
    <h1>Stecken-Rein</h1>
    <p>Your private neighborhood network.</p>

    <hr />

    <p><strong>Backend:</strong> {{ backendStatus }}</p>
  </div>
</template>