<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiDiaryDetail, apiDiaryRate, type DiaryDetailVO } from '../../lib/api'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const auth = useAuthStore()
const loading = ref(false)
const diary = ref<DiaryDetailVO | null>(null)

const rate = reactive({ rating: 5 })

function parseJsonList(s?: string) {
  if (!s) return []
  try {
    const v = JSON.parse(s)
    return Array.isArray(v) ? v : []
  } catch {
    return []
  }
}

async function load() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    diary.value = await apiDiaryDetail(id)
  } finally {
    loading.value = false
  }
}

async function submitRate() {
  if (!diary.value) return
  await apiDiaryRate({ diaryId: diary.value.id, rating: rate.rating })
  ElMessage.success('评分成功')
  await load()
}

onMounted(load)
</script>

<template>
  <div class="page" v-loading="loading">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center; gap: 12px">
          <div style="font-weight: 900">{{ diary?.title || '日记详情' }}</div>
          <div class="muted">热度 {{ diary?.heat ?? 0 }} · 评分 {{ diary?.rating ?? 0 }}</div>
        </div>
      </template>

      <div class="content">
        <div class="text">{{ diary?.content }}</div>

        <div v-if="parseJsonList(diary?.images).length" class="gallery">
          <div class="muted" style="margin-bottom: 6px">图片</div>
          <el-image
            v-for="(u, idx) in parseJsonList(diary?.images)"
            :key="idx"
            :src="u"
            fit="cover"
            style="width: 160px; height: 110px; border-radius: 12px"
          />
        </div>

        <div v-if="parseJsonList(diary?.videos).length" class="gallery">
          <div class="muted" style="margin-bottom: 6px">视频</div>
          <a v-for="(u, idx) in parseJsonList(diary?.videos)" :key="idx" :href="u" target="_blank" class="link">
            {{ u }}
          </a>
        </div>
      </div>

      <el-divider />

      <div class="glass rateBox">
        <div style="font-weight: 900">评分</div>
        <div class="muted" style="font-size: 12px; margin-top: 4px">
          后端接口：<code>/api/diary/rate</code>（需要登录）
        </div>

        <div v-if="auth.isAuthed" class="rateRow">
          <el-rate v-model="rate.rating" />
          <el-button type="primary" @click="submitRate">提交评分</el-button>
        </div>
        <div v-else class="muted" style="margin-top: 10px">
          请先 <a style="cursor: pointer; color: rgba(34,211,238,0.95)" @click="$router.push('/login')">登录</a> 后评分
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.content {
  padding: 6px 2px;
}
.text {
  white-space: pre-wrap;
  line-height: 1.75;
}
.gallery {
  margin-top: 12px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
.link {
  color: rgba(34, 211, 238, 0.95);
  text-decoration: none;
  font-size: 12px;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}
.rateBox {
  padding: 12px;
}
.rateRow {
  margin-top: 10px;
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}
</style>

