<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiDiaryCreate, apiDiaryDetail, apiDiaryUpdate } from '../../lib/api'

const route = useRoute()
const router = useRouter()
const loading = ref(false)

const isEdit = ref(false)
const diaryId = ref<number | null>(null)

const form = reactive({
  title: '',
  content: '',
  imagesText: '',
  videosText: '',
})

function splitLines(s: string) {
  return s
    .split('\n')
    .map((x) => x.trim())
    .filter(Boolean)
}

async function loadForEdit(id: number) {
  loading.value = true
  try {
    const d = await apiDiaryDetail(id)
    form.title = d.title
    form.content = d.content
    try {
      form.imagesText = (JSON.parse(d.images || '[]') as string[]).join('\n')
    } catch {
      form.imagesText = ''
    }
    try {
      form.videosText = (JSON.parse(d.videos || '[]') as string[]).join('\n')
    } catch {
      form.videosText = ''
    }
  } finally {
    loading.value = false
  }
}

async function submit() {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写标题与正文')
    return
  }
  loading.value = true
  try {
    const images = splitLines(form.imagesText)
    const videos = splitLines(form.videosText)
    if (isEdit.value && diaryId.value) {
      await apiDiaryUpdate(diaryId.value, { title: form.title, content: form.content, images, videos })
      ElMessage.success('更新成功')
      router.push(`/diary/${diaryId.value}`)
      return
    }
    const data = await apiDiaryCreate({ title: form.title, content: form.content, images, videos })
    ElMessage.success('创建成功')
    router.push(`/diary/${data.diary_id}`)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const id = route.params.id ? Number(route.params.id) : null
  if (id) {
    isEdit.value = true
    diaryId.value = id
    loadForEdit(id)
  }
})
</script>

<template>
  <div class="page" v-loading="loading">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div style="font-weight: 900">{{ isEdit ? '编辑日记' : '新建日记' }}</div>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-form label-position="top">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="正文">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="写下你的旅途故事..." />
        </el-form-item>

        <div class="grid">
          <el-form-item label="图片URL（每行一个，可选）">
            <el-input v-model="form.imagesText" type="textarea" :rows="5" placeholder="https://..." />
          </el-form-item>
          <el-form-item label="视频URL（每行一个，可选）">
            <el-input v-model="form.videosText" type="textarea" :rows="5" placeholder="https://..." />
          </el-form-item>
        </div>

        <div class="muted" style="font-size: 12px; margin-bottom: 10px">
          说明：后端 `Diary` 的 `images/videos` 字段以 JSON 字符串保存，前端这里用“每行一个URL”的方式输入，提交时会自动转为 JSON。
        </div>

        <el-button type="primary" size="large" :loading="loading" @click="submit">
          {{ isEdit ? '保存修改' : '发布日记' }}
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
@media (max-width: 860px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>

