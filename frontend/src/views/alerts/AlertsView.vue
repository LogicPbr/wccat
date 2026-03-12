<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

const alerts = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await api.get('/alerts')
    alerts.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="alerts-view">
    <h1>报警规则</h1>
    <p class="desc">在持仓或自选股上设置买卖提醒</p>
    <div v-if="loading">加载中...</div>
    <div v-else>
      <p v-if="!alerts.length" class="muted">暂无报警规则</p>
      <ul v-else>
        <li v-for="a in alerts" :key="a.id">{{ a.stockName }} - {{ a.conditionType }}</li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.desc { color: var(--color-text-muted); margin-bottom: 1rem; }
.muted { color: var(--color-text-muted); }
ul { padding-left: 1.5rem; }
</style>
