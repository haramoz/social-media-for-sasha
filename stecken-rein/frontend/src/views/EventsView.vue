<script setup lang="ts">
import { onMounted, ref } from 'vue'
import api from '../services/api'

type EventItem = {
  id: number
  createdBy: number
  creatorName: string
  title: string
  description: string
  location: string
  startTime: string
  endTime: string
  goingCount: number
  maybeCount: number
  notGoingCount: number
  myRsvp: string | null
}

const events = ref<EventItem[]>([])

const title = ref('')
const description = ref('')
const location = ref('')
const startTime = ref('')
const endTime = ref('')
const message = ref('')
const showCreateForm = ref(false)

const loadEvents = async () => {
  const response = await api.get('/events')
  events.value = response.data
}

const createEvent = async () => {
  if (!title.value || !startTime.value) {
    message.value = 'Title and start time are required.'
    return
  }

  await api.post('/events', {
    title: title.value,
    description: description.value,
    location: location.value,
    startTime: startTime.value,
    endTime: endTime.value || null,
  })

  title.value = ''
  description.value = ''
  location.value = ''
  startTime.value = ''
  endTime.value = ''
  message.value = 'Event created.'

  await loadEvents()
}

const rsvp = async (eventId: number, status: 'GOING' | 'MAYBE' | 'NOT_GOING') => {
  await api.post(`/events/${eventId}/rsvp`, { status })
  await loadEvents()
}

const formatDate = (value: string) =>
  new Date(value).toLocaleString()
  
onMounted(loadEvents)
</script>

<template>
  <div>
    <h1>Events</h1>

    <div class="card">
        <button class="secondary-button" @click="showCreateForm = !showCreateForm">
            {{ showCreateForm ? 'Hide create event' : '+ Create new event' }}
        </button>

        <div v-if="showCreateForm" class="create-form">
            <h2>Create Event</h2>

            <input v-model="title" placeholder="Title" />
            <input v-model="location" placeholder="Location" />

            <label>Start</label>
            <input v-model="startTime" type="datetime-local" />

            <label>End</label>
            <input v-model="endTime" type="datetime-local" />

            <textarea
            v-model="description"
            placeholder="Description"
            rows="3"
            />

            <button @click="createEvent">Create Event</button>

            <p>{{ message }}</p>
        </div>
    </div>

    <div v-for="event in events" :key="event.id" class="card">
    <h2>{{ event.title }}</h2>

    <p v-if="event.location">📍 {{ event.location }}</p>
    <p>🕒 {{ formatDate(event.startTime) }}</p>
    <p v-if="event.description">{{ event.description }}</p>

    <p class="rsvp-counts">
        Going: {{ event.goingCount }} |
        Maybe: {{ event.maybeCount }} |
        Not going: {{ event.notGoingCount }}
    </p>

    <div class="rsvp-buttons">
        <button
        :class="{ selected: event.myRsvp === 'GOING' }"
        @click="rsvp(event.id, 'GOING')"
        >
        Going
        </button>

        <button
        :class="{ selected: event.myRsvp === 'MAYBE' }"
        @click="rsvp(event.id, 'MAYBE')"
        >
        Maybe
        </button>

        <button
        :class="{ selected: event.myRsvp === 'NOT_GOING' }"
        @click="rsvp(event.id, 'NOT_GOING')"
        >
        Can't go
        </button>
    </div>

    <small>Created by {{ event.creatorName }}</small>
    </div>
  </div>
</template>

<style scoped>
.card {
  background: white;
  padding: 16px;
  border-radius: 16px;
  margin-bottom: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

input,
textarea,
button {
  width: 100%;
  box-sizing: border-box;
  padding: 12px;
  margin-top: 10px;
  font-size: 16px;
  border-radius: 10px;
}

input,
textarea {
  border: 1px solid #ccc;
}

button {
  border: none;
  background: #2563eb;
  color: white;
  font-weight: bold;
}

label {
  display: block;
  margin-top: 12px;
  font-weight: 600;
}

.secondary-button {
  background: white;
  color: #2563eb;
  border: 1px solid #2563eb;
}

.create-form {
  margin-top: 16px;
}

.rsvp-counts {
  font-weight: 600;
  color: #444;
}

.rsvp-buttons {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 8px;
  margin: 12px 0;
}

.rsvp-buttons button {
  background: #f3f4f6;
  color: #333;
}

.rsvp-buttons button.selected {
  background: #2563eb;
  color: white;
}
</style>