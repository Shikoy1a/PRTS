<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { apiScenicDetail, type ScenicArea } from '../../lib/api'

const route = useRoute()
const loading = ref(false)
const scenic = ref<ScenicArea | null>(null)

async function load() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    scenic.value = await apiScenicDetail(id)
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page" v-loading="loading">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center; gap: 12px">
          <div style="font-weight: 900; font-size: 18px">{{ scenic?.name || '景区详情' }}</div>
          <el-tag effect="dark" type="info">{{ scenic?.type || '—' }}</el-tag>
        </div>
      </template>

      <div class="grid">
        <div class="glass block">
          <div class="k">位置</div>
          <div class="v">{{ scenic?.location || '—' }}</div>
        </div>
        <div class="glass block">
          <div class="k">开放时间</div>
          <div class="v">{{ scenic?.openTime || '—' }}</div>
        </div>
        <div class="glass block">
          <div class="k">票价</div>
          <div class="v">{{ scenic?.ticketPrice || '—' }}</div>
        </div>
        <div class="glass block">
          <div class="k">热度 / 评分</div>
          <div class="v">{{ scenic?.heat ?? 0 }} / {{ scenic?.rating ?? 0 }}</div>
        </div>
      </div>

      <div class="glass block" style="margin-top: 12px">
        <div class="k">简介</div>
        <div class="v2">{{ scenic?.description || '暂无简介' }}</div>
      </div>

      <div class="glass block" style="margin-top: 12px">
        <div class="k">经纬度</div>
        <div class="v2">
          {{ scenic?.latitude ?? '—' }}, {{ scenic?.longitude ?? '—' }}
          <span class="muted" style="margin-left: 8px">(用于设施“附近查询”与美食“距离权重”)</span>
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
@media (max-width: 820px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>

