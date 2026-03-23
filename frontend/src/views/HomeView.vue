<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  apiRecommendationHot,
  apiRecommendationList,
  apiRecommendationPersonalized,
  type PageData,
  type ScenicArea,
  type ScenicAreaRecommendVO,
} from '../lib/api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const tab = ref<'recommend' | 'hot' | 'personalized'>('recommend')

const query = reactive({
  page: 1,
  size: 8,
  type: '' as string | '',
})

const loading = ref(false)
const list = ref<ScenicArea[]>([])
const total = ref(0)
const pList = ref<ScenicAreaRecommendVO[]>([])
const pTotal = ref(0)

const canPersonal = computed(() => auth.isAuthed)

async function load() {
  loading.value = true
  try {
    if (tab.value === 'hot') {
      const data: PageData<ScenicArea> = await apiRecommendationHot({
        page: query.page,
        size: query.size,
        type: query.type || undefined,
      })
      list.value = data.list
      total.value = data.total
      return
    }
    if (tab.value === 'personalized') {
      if (!canPersonal.value) {
        ElMessage.info('登录后可使用个性化推荐')
        tab.value = 'recommend'
        return
      }
      const data = await apiRecommendationPersonalized({
        page: query.page,
        size: query.size,
        type: query.type || undefined,
      })
      pList.value = data.list
      pTotal.value = data.total
      return
    }

    const data = await apiRecommendationList({
      page: query.page,
      size: query.size,
      sortBy: 'heat',
      type: query.type || undefined,
    })
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page">
    <div class="hero glass">
      <div class="h1">探索你的下一段旅程</div>
      <div class="muted">
        热门景区、个性化推荐、路线规划与周边设施一站式体验。后端返回统一为 <code>{code,data,message}</code>，前端已做统一处理。
      </div>
      <div class="filters">
        <el-segmented v-model="tab" :options="[
          { label: '推荐列表', value: 'recommend' },
          { label: '热门景区', value: 'hot' },
          { label: '个性化', value: 'personalized' },
        ]" />
        <el-input v-model="query.type" placeholder="类型（可选，例如：校园/普通景区）" style="max-width: 320px" clearable />
        <el-button type="primary" :loading="loading" @click="query.page = 1; load()">刷新</el-button>
      </div>
    </div>

    <div class="grid" v-loading="loading">
      <template v-if="tab !== 'personalized'">
        <el-card v-for="s in list" :key="s.id" class="card" shadow="never" @click="$router.push(`/scenic/${s.id}`)">
          <div class="card-title">{{ s.name }}</div>
          <div class="muted line">{{ s.location || '—' }}</div>
          <div class="muted line">{{ s.description || '暂无简介' }}</div>
          <div class="meta">
            <el-tag effect="dark" type="info">{{ s.type || '未知类型' }}</el-tag>
            <div class="muted">热度 {{ s.heat ?? 0 }} · 评分 {{ s.rating ?? 0 }}</div>
          </div>
        </el-card>
      </template>

      <template v-else>
        <el-card v-for="s in pList" :key="s.id" class="card" shadow="never" @click="$router.push(`/scenic/${s.id}`)">
          <div class="card-title">{{ s.name }}</div>
          <div class="muted line">{{ s.reason || '为你推荐' }}</div>
          <div class="meta">
            <el-tag effect="dark" type="success">匹配度 {{ (s.score ?? 0).toFixed(2) }}</el-tag>
            <div class="muted">热度 {{ s.heat ?? 0 }} · 评分 {{ s.rating ?? 0 }}</div>
          </div>
        </el-card>
      </template>
    </div>

    <div class="pager">
      <el-pagination
        background
        layout="prev, pager, next, total"
        :page-size="query.size"
        :current-page="query.page"
        :total="tab === 'personalized' ? pTotal : total"
        @current-change="(p:number)=>{query.page=p; load()}"
      />
    </div>
  </div>
</template>

<style scoped>
.hero {
  padding: 18px;
}
.h1 {
  font-size: 28px;
  font-weight: 900;
  margin-bottom: 6px;
}
.filters {
  margin-top: 14px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
.grid {
  margin-top: 14px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}
.card {
  cursor: pointer;
  background: rgba(255, 255, 255, 0.05);
}
.card-title {
  font-weight: 800;
  font-size: 16px;
  margin-bottom: 6px;
}
.line {
  font-size: 12px;
  margin-bottom: 6px;
}
.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}
.pager {
  margin-top: 14px;
  display: flex;
  justify-content: center;
}
@media (max-width: 1200px) {
  .grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
@media (max-width: 640px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>

