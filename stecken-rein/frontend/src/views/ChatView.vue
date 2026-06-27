<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'


type ChatMessage = {
  id: number
  senderId: number
  receiverId: number
  senderName: string
  text: string
  imagePath: string | null
  createdAt: string
}

const route = useRoute()
const authStore = useAuthStore()
let refreshTimer: number | undefined

const neighborId = Number(route.params.neighborId)

const messages = ref<ChatMessage[]>([])
const newMessage = ref('')
const selectedImage = ref<File | null>(null)
const message = ref('')

const loadMessages = async () => {
  const response = await api.get(`/chat/${neighborId}`)
  messages.value = response.data
}

const onImageSelected = (event: Event) => {
  const input = event.target as HTMLInputElement
  selectedImage.value = input.files?.[0] ?? null
}

const sendMessage = async () => {
  if (!newMessage.value.trim() && !selectedImage.value) {
    message.value = 'Please write a message or choose a photo.'
    return
  }

  const formData = new FormData()
  formData.append('text', newMessage.value)

  if (selectedImage.value) {
    formData.append('image', selectedImage.value)
  }

  await api.post(`/chat/${neighborId}`, formData)

  newMessage.value = ''
  selectedImage.value = null
  message.value = ''

  await loadMessages()
}

const formatDate = (value: string) =>
  new Date(value).toLocaleString()

onMounted(async () => {
  await loadMessages()

  refreshTimer = window.setInterval(() => {
    loadMessages()
  }, 3000)
})

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
})

</script>

<template>
  <div>
    <h1>Chat</h1>

    <div class="messages">
      <div
        v-for="chat in messages"
        :key="chat.id"
        class="message-bubble"
        :class="{ mine: chat.senderId === authStore.currentUser?.id }"
      >
        <small>{{ chat.senderName }}</small>

        <p v-if="chat.text">{{ chat.text }}</p>

        <div v-if="chat.imagePath" class="chat-image-wrapper">
          <img
            :src="`http://localhost:8090${chat.imagePath}`"
            class="chat-image"
          />
        </div>

        <small class="time">{{ formatDate(chat.createdAt) }}</small>
      </div>
    </div>

    <div class="composer">
      <textarea
        v-model="newMessage"
        placeholder="Write a private message..."
        rows="2"
      />

      <input type="file" accept="image/*" @change="onImageSelected" />

      <button @click="sendMessage">Send</button>

      <p>{{ message }}</p>
    </div>
  </div>
</template>

<style scoped>
.messages {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 180px;
}

.message-bubble {
  max-width: 78%;
  background: white;
  padding: 12px;
  border-radius: 16px;
  align-self: flex-start;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.message-bubble.mine {
  align-self: flex-end;
  background: #dbeafe;
}

.message-bubble p {
  margin: 6px 0;
}

.time {
  color: #777;
  font-size: 12px;
}

.chat-image-wrapper {
  width: 100%;
  max-height: 260px;
  border-radius: 12px;
  overflow: hidden;
  margin-top: 8px;
}

.chat-image {
  width: 100%;
  max-height: 260px;
  object-fit: cover;
  display: block;
}

.composer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 58px;
  background: white;
  border-top: 1px solid #ddd;
  padding: 12px;
}

textarea,
input,
button {
  width: 100%;
  box-sizing: border-box;
  margin-top: 8px;
  padding: 10px;
  border-radius: 10px;
  font-size: 16px;
}

textarea,
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