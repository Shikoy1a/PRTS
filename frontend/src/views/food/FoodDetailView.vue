<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiFoodDetail, apiFoodRate, type Food } from '../../lib/api'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const auth = useAuthStore()
const loading = ref(false)
const food = ref<Food | null>(null)

const rate = reactive({
  rating: 5,
  comment: '',
})

async function load() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    food.value = await apiFoodDetail(id)
  } finally {
    loading.value = false
  }
}

async function submitRate() {
  if (!food.value) return
  await apiFoodRate({ foodId: food.value.id, rating: rate.rating, comment: rate.comment || undefined })
  ElMessage.success('评分成功')
  await load()
}

onMounted(load)
</script>

<template>
  <div class="page" v-loading="loading">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div style="font-weight: 900">{{ food?.name || '美食详情' }}</div>
          <el-tag effect="dark" type="info">{{ food?.cuisine || '—' }}</el-tag>
        </div>
      </template>

      <div class="grid">
        <div class="glass block">
          <div class="k">价格</div>
          <div class="v">¥ {{ food?.price ?? 0 }}</div>
        </div>
        <div class="glass block">
          <div class="k">热度 / 评分</div>
          <div class="v">{{ food?.heat ?? 0 }} / {{ food?.rating ?? 0 }}</div>
        </div>
        <div class="glass block">
          <div class="k">areaId</div>
          <div class="v">{{ food?.areaId ?? '—' }}</div>
        </div>
        <div class="glass block">
          <div class="k">restaurantId</div>
          <div class="v">{{ food?.restaurantId ?? '—' }}</div>
        </div>
      </div>

      <div class="glass block" style="margin-top: 12px">
        <div class="k">描述</div>
        <div class="v2">{{ food?.description || '暂无描述' }}</div>
      </div>

      <el-divider />

      <div class="glass block">
        <div style="font-weight: 900">评分</div>
        <div class="muted" style="font-size: 12px; margin-top: 4px">
          后端接口：<code>/api/food/rate</code>（需要登录，携带 <code>Authorization: Bearer token</code>）
        </div>

        <div v-if="auth.isAuthed" class="rateBox">
          <el-rate v-model="rate.rating" />
          <el-input v-model="rate.comment" type="textarea" :rows="3" placeholder="写点评价（可选）" />
          <el-button type="primary" @click="submitRate">提交</el-button>
        </div>
        <div v-else class="muted" style="margin-top: 10px">
          请先 <a style="cursor: pointer; color: rgba(34,211,238,0.95)" @click="$router.push('/login')">登录</a> 后评分
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}
.block {
  padding: 14px;
}
.k {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.62);
}
.v {
  font-size: 14px;
  font-weight: 800;
  margin-top: 6px;
}
.v2 {
  font-size: 13px;
  margin-top: 6px;
  line-height: 1.6;
}
.rateBox {
  margin-top: 10px;
  display: grid;
  gap: 10px;
}
</style>

