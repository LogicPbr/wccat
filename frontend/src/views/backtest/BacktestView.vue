<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

const backtests = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await api.get('/backtests')
    backtests.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="backtest-view">
    <h1>回测</h1>
    <p class="desc">对策略进行历史数据回测</p>
    <div v-if="loading">加载中...</div>
    <div v-else class="empty">暂无回测记录，后端 API 已就绪</div>
  </div>
</template>

<style scoped>
.desc { color: var(--color-text-muted); margin-bottom: 1.5rem; }
.empty { color: var(--color-text-muted); padding: 2rem; }
</style>
