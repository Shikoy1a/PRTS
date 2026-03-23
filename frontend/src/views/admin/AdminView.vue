<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  apiAdminAddBuilding,
  apiAdminAddFood,
  apiAdminAddRoad,
  apiAdminAddScenicArea,
  apiAdminListScenicAreas,
  type ScenicArea,
} from '../../lib/api'

const loading = ref(false)
const scenicList = ref<ScenicArea[]>([])
const total = ref(0)
const q = reactive({ page: 1, size: 10, type: '' as string | '' })

const scenicForm = reactive<Partial<ScenicArea>>({
  name: '',
  description: '',
  location: '',
  longitude: undefined,
  latitude: undefined,
  type: '',
  openTime: '',
  ticketPrice: '',
})

const buildingForm = reactive<any>({ name: '', type: '', description: '', location: '', longitude: null, latitude: null, areaId: null })
const roadForm = reactive<any>({ startId: null, endId: null, distance: 0, speed: 0, congestion: 1, vehicleType: 'walk,bike,shuttle', areaId: null })
const foodForm = reactive<any>({ name: '', cuisine: '', description: '', price: 0, areaId: null, restaurantId: null })

async function loadScenic() {
  loading.value = true
  try {
    const data = await apiAdminListScenicAreas({
      page: q.page,
      size: q.size,
      type: q.type || undefined,
    })
    scenicList.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

async function addScenic() {
  await apiAdminAddScenicArea(scenicForm)
  ElMessage.success('添加景区成功')
  await loadScenic()
}

async function addBuilding() {
  await apiAdminAddBuilding(buildingForm)
  ElMessage.success('添加建筑成功')
}

async function addRoad() {
  await apiAdminAddRoad(roadForm)
  ElMessage.success('添加道路成功')
}

async function addFood() {
  await apiAdminAddFood(foodForm)
  ElMessage.success('添加美食成功')
}

onMounted(loadScenic)
</script>

<template>
  <div class="page">
    <el-card class="glass" shadow="never">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div style="font-weight: 900">管理员数据管理</div>
          <div class="muted" style="font-size: 12px">后端会校验 role=ADMIN，否则返回 403</div>
        </div>
      </template>

      <el-tabs>
        <el-tab-pane label="景区管理">
          <div class="grid">
            <div class="glass block">
              <div style="font-weight: 900; margin-bottom: 10px">新增景区</div>
              <el-form label-position="top">
                <el-form-item label="name">
                  <el-input v-model="scenicForm.name" />
                </el-form-item>
                <el-form-item label="type">
                  <el-input v-model="scenicForm.type" placeholder="例如：校园/普通景区" />
                </el-form-item>
                <el-form-item label="location">
                  <el-input v-model="scenicForm.location" />
                </el-form-item>
                <div class="row">
                  <el-form-item label="latitude">
                    <el-input-number v-model="scenicForm.latitude" :step="0.0001" style="width: 100%" />
                  </el-form-item>
                  <el-form-item label="longitude">
                    <el-input-number v-model="scenicForm.longitude" :step="0.0001" style="width: 100%" />
                  </el-form-item>
                </div>
                <el-form-item label="openTime">
                  <el-input v-model="scenicForm.openTime" />
                </el-form-item>
                <el-form-item label="ticketPrice">
                  <el-input v-model="scenicForm.ticketPrice" />
                </el-form-item>
                <el-form-item label="description">
                  <el-input v-model="scenicForm.description" type="textarea" :rows="4" />
                </el-form-item>
                <el-button type="primary" :loading="loading" @click="addScenic">提交</el-button>
              </el-form>
            </div>

            <div class="glass block">
              <div style="font-weight: 900; margin-bottom: 10px">景区列表</div>
              <div class="formRow">
                <el-input v-model="q.type" placeholder="type(可选)" clearable />
                <el-button @click="q.page=1; loadScenic()" :loading="loading">查询</el-button>
              </div>
              <el-table :data="scenicList" v-loading="loading" style="width: 100%; margin-top: 10px">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="name" label="名称" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="location" label="位置" />
              </el-table>
              <div style="margin-top: 10px; display: flex; justify-content: center">
                <el-pagination
                  background
                  layout="prev, pager, next, total"
                  :page-size="q.size"
                  :current-page="q.page"
                  :total="total"
                  @current-change="(p:number)=>{q.page=p; loadScenic()}"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="建筑 / 道路 / 美食">
          <div class="grid3">
            <div class="glass block">
              <div style="font-weight: 900; margin-bottom: 10px">新增建筑</div>
              <el-form label-position="top">
                <el-form-item label="name"><el-input v-model="buildingForm.name" /></el-form-item>
                <el-form-item label="type"><el-input v-model="buildingForm.type" /></el-form-item>
                <el-form-item label="location"><el-input v-model="buildingForm.location" /></el-form-item>
                <el-form-item label="areaId"><el-input-number v-model="buildingForm.areaId" :min="1" style="width: 100%" /></el-form-item>
                <el-button type="primary" @click="addBuilding">提交</el-button>
              </el-form>
            </div>

            <div class="glass block">
              <div style="font-weight: 900; margin-bottom: 10px">新增道路</div>
              <el-form label-position="top">
                <div class="row">
                  <el-form-item label="startId"><el-input-number v-model="roadForm.startId" :min="1" style="width: 100%" /></el-form-item>
                  <el-form-item label="endId"><el-input-number v-model="roadForm.endId" :min="1" style="width: 100%" /></el-form-item>
                </div>
                <el-form-item label="distance"><el-input-number v-model="roadForm.distance" :min="0" style="width: 100%" /></el-form-item>
                <el-form-item label="speed"><el-input-number v-model="roadForm.speed" :min="0" style="width: 100%" /></el-form-item>
                <el-form-item label="congestion"><el-input-number v-model="roadForm.congestion" :min="0.1" :step="0.1" style="width: 100%" /></el-form-item>
                <el-form-item label="vehicleType"><el-input v-model="roadForm.vehicleType" placeholder="walk,bike,shuttle" /></el-form-item>
                <el-form-item label="areaId"><el-input-number v-model="roadForm.areaId" :min="1" style="width: 100%" /></el-form-item>
                <el-button type="primary" @click="addRoad">提交</el-button>
              </el-form>
            </div>

            <div class="glass block">
              <div style="font-weight: 900; margin-bottom: 10px">新增美食</div>
              <el-form label-position="top">
                <el-form-item label="name"><el-input v-model="foodForm.name" /></el-form-item>
                <el-form-item label="cuisine"><el-input v-model="foodForm.cuisine" /></el-form-item>
                <el-form-item label="price"><el-input-number v-model="foodForm.price" :min="0" style="width: 100%" /></el-form-item>
                <el-form-item label="areaId"><el-input-number v-model="foodForm.areaId" :min="1" style="width: 100%" /></el-form-item>
                <el-form-item label="restaurantId"><el-input-number v-model="foodForm.restaurantId" :min="1" style="width: 100%" /></el-form-item>
                <el-form-item label="description"><el-input v-model="foodForm.description" type="textarea" :rows="3" /></el-form-item>
                <el-button type="primary" @click="addFood">提交</el-button>
              </el-form>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 12px;
}
.grid3 {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}
.block {
  padding: 14px;
}
.row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
.formRow {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}
@media (max-width: 1080px) {
  .grid {
    grid-template-columns: 1fr;
  }
  .grid3 {
    grid-template-columns: 1fr;
  }
}
</style>

