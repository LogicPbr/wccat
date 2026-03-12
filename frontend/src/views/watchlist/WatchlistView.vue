<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { api } from '@/api/client'

interface WatchlistStock {
  id: number
  stockCode: string
  stockName: string
  market: string
  currentPrice: number | null
  prevClose: number | null
  change: number | null
  changePercent: number | null
  createdAt: string
  priceUpdatedAt: string | null
}

const stocks = ref<WatchlistStock[]>([])
const loading = ref(false)
const adding = ref(false)
const refreshing = ref(false)
const addCode = ref('')
const addError = ref('')
const importText = ref('')
const importing = ref(false)
const importResult = ref<{ added: number; skipped: number; failed: number; failures: { stockCode: string; reason: string }[] } | null>(null)

async function fetchStocks() {
  loading.value = true
  addError.value = ''
  try {
    const res = await api.get<{ data: WatchlistStock[] }>('/watchlist/stocks')
    stocks.value = res.data || []
  } catch (e: any) {
    addError.value = e?.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function addStock() {
  const code = addCode.value.trim()
  if (!code) {
    addError.value = '请输入6位股票代码'
    return
  }
  if (!/^\d{6}$/.test(code)) {
    addError.value = '股票代码需为6位数字'
    return
  }
  adding.value = true
  addError.value = ''
  try {
    const res = await api.post<{ data: WatchlistStock }>('/watchlist/stocks', { stockCode: code })
    stocks.value = [res.data, ...stocks.value]
    addCode.value = ''
  } catch (e: any) {
    addError.value = e?.message || '添加失败'
  } finally {
    adding.value = false
  }
}

async function refreshPrices() {
  refreshing.value = true
  addError.value = ''
  try {
    const res = await api.post<{ data: WatchlistStock[] }>('/watchlist/stocks/refresh')
    stocks.value = res.data || []
  } catch (e: any) {
    addError.value = e?.message || '刷新失败'
  } finally {
    refreshing.value = false
  }
}

async function removeStock(id: number) {
  try {
    await api.delete(`/watchlist/stocks/${id}`)
    stocks.value = stocks.value.filter(s => s.id !== id)
  } catch (e: any) {
    addError.value = e?.message || '删除失败'
  }
}

async function importFromTonghuashun() {
  if (!importText.value.trim()) {
    addError.value = '请粘贴同花顺导出的内容'
    return
  }
  importing.value = true
  addError.value = ''
  importResult.value = null
  try {
    const res = await api.post<{ data: typeof importResult.value }>('/watchlist/stocks/import-from-tonghuashun', {
      text: importText.value
    })
    importResult.value = res.data
    if (res.data && res.data.added > 0) {
      const refreshRes = await api.post<{ data: WatchlistStock[] }>('/watchlist/stocks/refresh')
      stocks.value = refreshRes.data || []
    }
  } catch (e: any) {
    addError.value = e?.message || '导入失败'
  } finally {
    importing.value = false
  }
}

function formatPrice(v: number | null | undefined): string {
  if (v == null) return '-'
  return v.toFixed(2)
}

function formatPercent(v: number | null | undefined): string {
  if (v == null) return '-'
  const s = v >= 0 ? `+${v.toFixed(2)}` : v.toFixed(2)
  return s + '%'
}

onMounted(async () => {
  await fetchStocks()
  if (stocks.value.length > 0) {
    refreshing.value = true
    try {
      const res = await api.post<{ data: WatchlistStock[] }>('/watchlist/stocks/refresh')
      stocks.value = res.data || []
    } finally {
      refreshing.value = false
    }
  }
})
</script>

<template>
  <div class="watchlist-view">
    <div class="header">
      <div>
        <h1>自选股</h1>
        <p class="desc">添加股票代码，实时获取行情（数据来源：东方财富）</p>
      </div>
      <div class="actions">
        <button
          class="btn btn-primary"
          :disabled="refreshing"
          @click="refreshPrices"
        >
          {{ refreshing ? '刷新中...' : '刷新行情' }}
        </button>
      </div>
    </div>

    <div class="add-form">
      <input
        v-model="addCode"
        type="text"
        placeholder="输入6位股票代码，如 600519"
        maxlength="6"
        class="input"
        :disabled="adding"
        @keyup.enter="addStock"
      />
      <button
        class="btn btn-primary"
        :disabled="adding || !addCode.trim()"
        @click="addStock"
      >
        {{ adding ? '添加中...' : '添加' }}
      </button>
    </div>
    <p v-if="addError" class="error">{{ addError }}</p>

    <div class="import-section">
      <h3>从同花顺导入</h3>
      <p class="import-desc">
        在同花顺中：自选股 → 全选(Ctrl+A) → 右键 → 数据导出 → 导出选中股票，保存为 txt 或 Excel。
        打开文件复制内容，粘贴到下方并点击导入。
      </p>
      <textarea
        v-model="importText"
        placeholder="粘贴导出的内容，如：&#10;600519	贵州茅台	1401.98&#10;000001	平安银行	12.35"
        class="import-textarea"
        rows="4"
        :disabled="importing"
      />
      <div class="import-actions">
        <button
          class="btn btn-secondary"
          :disabled="importing || !importText.trim()"
          @click="importFromTonghuashun"
        >
          {{ importing ? '导入中...' : '导入' }}
        </button>
      </div>
      <div v-if="importResult" class="import-result">
        <span class="r-added">新增 {{ importResult.added }} 只</span>
        <span v-if="importResult.skipped" class="r-skipped">已存在 {{ importResult.skipped }} 只</span>
        <span v-if="importResult.failed" class="r-failed">失败 {{ importResult.failed }} 只</span>
        <div v-if="importResult.failures?.length" class="failures">
          <span v-for="f in importResult.failures" :key="f.stockCode">{{ f.stockCode }}: {{ f.reason }}</span>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="!stocks.length" class="empty">
      <p>暂无自选股，输入股票代码添加</p>
      <p class="hint">沪市 6 开头、深市 0/3 开头、北交所 4/8 开头</p>
    </div>
    <div v-else class="table-wrap">
      <table class="stock-table">
        <thead>
          <tr>
            <th>代码</th>
            <th>名称</th>
            <th>现价</th>
            <th>涨跌额</th>
            <th>涨跌幅</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in stocks" :key="s.id">
            <td class="code">{{ s.stockCode }}</td>
            <td class="name">{{ s.stockName || '-' }}</td>
            <td>{{ formatPrice(s.currentPrice) }}</td>
            <td :class="s.change != null && s.change >= 0 ? 'up' : 'down'">
              {{ s.change != null ? (s.change >= 0 ? '+' : '') + s.change.toFixed(2) : '-' }}
            </td>
            <td :class="s.changePercent != null && s.changePercent >= 0 ? 'up' : 'down'">
              {{ formatPercent(s.changePercent) }}
            </td>
            <td>
              <button class="btn-link" @click="removeStock(s.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1.5rem;
}
.header h1 {
  margin: 0 0 0.25rem;
}
.desc {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin: 0;
}
.actions {
  flex-shrink: 0;
}

.add-form {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}
.input {
  width: 220px;
  padding: 0.5rem 0.75rem;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  color: var(--color-text);
  font-size: 0.95rem;
}
.input::placeholder {
  color: var(--color-text-muted);
  opacity: 0.8;
}
.input:focus {
  outline: none;
  border-color: var(--color-primary);
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-size: 0.9rem;
}
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.btn-primary {
  background: var(--color-primary);
  color: var(--color-bg);
}
.btn-secondary {
  background: var(--color-surface-elevated);
  color: var(--color-text);
  border: 1px solid var(--color-border);
}

.import-section {
  margin-top: 2rem;
  padding: 1.25rem;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
}
.import-section h3 {
  margin: 0 0 0.5rem;
  font-size: 1rem;
}
.import-desc {
  color: var(--color-text-muted);
  font-size: 0.85rem;
  margin: 0 0 0.75rem;
  line-height: 1.5;
}
.import-textarea {
  width: 100%;
  padding: 0.75rem;
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  color: var(--color-text);
  font-size: 0.9rem;
  font-family: var(--font-mono);
  resize: vertical;
  box-sizing: border-box;
}
.import-textarea::placeholder {
  color: var(--color-text-muted);
}
.import-actions {
  margin-top: 0.5rem;
}
.import-result {
  margin-top: 0.75rem;
  font-size: 0.9rem;
  color: var(--color-text-muted);
}
.import-result .r-added { color: var(--color-primary); margin-right: 1rem; }
.import-result .r-skipped { margin-right: 1rem; }
.import-result .r-failed { color: var(--color-danger); }
.import-result .failures {
  margin-top: 0.5rem;
  font-size: 0.8rem;
  opacity: 0.9;
}

.error {
  color: var(--color-danger);
  font-size: 0.9rem;
  margin: 0 0 1rem;
}
.loading, .empty {
  color: var(--color-text-muted);
  padding: 2rem;
  text-align: center;
}
.empty .hint {
  font-size: 0.85rem;
  margin-top: 0.5rem;
  opacity: 0.8;
}

.table-wrap {
  overflow-x: auto;
  margin-top: 1rem;
}
.stock-table {
  width: 100%;
  border-collapse: collapse;
  background: var(--color-surface);
  border-radius: var(--radius);
  overflow: hidden;
}
.stock-table th,
.stock-table td {
  padding: 0.75rem 1rem;
  text-align: left;
  border-bottom: 1px solid var(--color-border);
}
.stock-table th {
  background: var(--color-surface-elevated);
  color: var(--color-text-muted);
  font-weight: 500;
  font-size: 0.85rem;
}
.stock-table tbody tr:hover {
  background: var(--color-surface-elevated);
}
.stock-table .code {
  font-family: var(--font-mono);
  font-weight: 500;
}
.stock-table .name {
  min-width: 80px;
}
.stock-table .up {
  color: var(--color-danger);
}
.stock-table .down {
  color: var(--color-primary);
}
.btn-link {
  background: none;
  border: none;
  color: var(--color-text-muted);
  cursor: pointer;
  font-size: 0.85rem;
  padding: 0;
  text-decoration: underline;
}
.btn-link:hover {
  color: var(--color-danger);
}
</style>
