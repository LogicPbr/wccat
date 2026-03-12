<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

const connections = ref<any[]>([])
const positions = ref<any[]>([])
const loading = ref(false)

async function fetch() {
  loading.value = true
  try {
    const [connRes, posRes] = await Promise.all([
      api.get('/broker/connections'),
      api.get('/broker/positions')
    ])
    connections.value = connRes.data || []
    positions.value = posRes.data || []
  } finally {
    loading.value = false
  }
}

function sync() {
  api.post('/broker/positions/sync').then(fetch)
}

onMounted(fetch)
</script>

<template>
  <div class="broker-view">
    <h1>券商账户</h1>
    <p class="desc">连接平安证券、华宝证券，获取持仓</p>
    <button class="btn" @click="sync">同步持仓</button>
    <div v-if="loading">加载中...</div>
    <div v-else>
      <h3>已连接</h3>
      <pre v-if="connections.length">{{ connections }}</pre>
      <p v-else class="muted">暂无连接</p>
      <h3>持仓</h3>
      <pre v-if="positions.length">{{ positions }}</pre>
      <p v-else class="muted">暂无持仓数据</p>
    </div>
  </div>
</template>

<style scoped>
.desc { color: var(--color-text-muted); margin-bottom: 1rem; }
.btn {
  background: var(--color-primary);
  color: var(--color-bg);
  border: none;
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  cursor: pointer;
  margin-bottom: 1.5rem;
}
.muted { color: var(--color-text-muted); }
pre { font-size: 0.85rem; overflow: auto; }
</style>
