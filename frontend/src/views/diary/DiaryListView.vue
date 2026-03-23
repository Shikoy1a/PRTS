<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import { apiDiaryDelete, apiDiaryList, apiDiarySearch, type Diary } from '../../lib/api'
import { useAuthStore } from '../../stores/auth'

const auth = useAuthStore()
const loading = ref(false)
const tab = ref<'list' | 'search'>('list')

const listQuery = reactive({ page: 1, size: 10, sortBy: 'createTime' })
const list = ref<Diary[]>([])

const searchQuery = reactive({ keyword: '', destination: undefined as number | undefined, page: 1, size: 10 })
const searchList = ref<Diary[]>([])

async function load() {
  loading.value = true
  try {
    list.value = await apiDiaryList(listQuery)
  } finally {
    loading.value = false
  }
}

async function search() {
  loading.value = true
  try {
    searchList.value = await apiDiarySearch({
      keyword: searchQuery.keyword || undefined,
      destination: searchQuery.destination,
      page: searchQuery.page,
      size: searchQuery.size,
    })
  } finally {
    loading.value = false
  }
}

async function del(row: Diary) {
  await ElMessageBox.confirm('确认删除该日记？此操作不可恢复。', '警告', { type: 'warning' })
  await apiDiaryDelete(row.id)
  await load()
}

onMounted(load)
</script>

<template>
  <div class="page">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div style="font-weight: 900">旅游日记</div>
          <el-button type="primary" :disabled="!auth.isAuthed" @click="$router.push('/diary/new')">写日记</el-button>
        </div>
      </template>

      <el-tabs v-model="tab" @tab-change="(n: string | number)=>{ if(n==='list') load(); else search(); }">
        <el-tab-pane label="列表" name="list">
          <el-table :data="list" v-loading="loading" style="width: 100%" @row-click="(r: Diary)=>$router.push(`/diary/${r.id}`)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="heat" label="热度" width="120" />
            <el-table-column prop="rating" label="评分" width="120" />
            <el-table-column label="操作" width="220">
              <template #default="{ row }">
                <el-button size="small" @click.stop="$router.push(`/diary/${row.id}`)">查看</el-button>
                <el-button size="small" :disabled="!auth.isAuthed" @click.stop="$router.push(`/diary/${row.id}/edit`)">
                  编辑
                </el-button>
                <el-button size="small" type="danger" :disabled="!auth.isAuthed" @click.stop="del(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="搜索" name="search">
          <div class="formRow">
            <el-input v-model="searchQuery.keyword" placeholder="keyword(可选)" clearable />
            <el-input-number v-model="searchQuery.destination" :min="1" placeholder="destination(可选)" />
            <el-button type="primary" :loading="loading" @click="search">搜索</el-button>
          </div>
          <el-table :data="searchList" v-loading="loading" style="width: 100%; margin-top: 12px" @row-click="(r: Diary)=>$router.push(`/diary/${r.id}`)">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="heat" label="热度" width="120" />
            <el-table-column prop="rating" label="评分" width="120" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div class="muted" style="margin-top: 10px; font-size: 12px">
        提示：创建/编辑/删除/评分接口均需要登录（后端会返回 401）。
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.formRow {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
</style>

