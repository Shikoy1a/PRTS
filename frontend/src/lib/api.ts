import { http, type ApiResponse } from './http'
import type { UserVO } from '../stores/auth'

export type PageData<T> = { list: T[]; total: number }

export type ScenicArea = {
  id: number
  name: string
  description?: string
  location?: string
  longitude?: number
  latitude?: number
  type?: string
  rating?: number
  heat?: number
  openTime?: string
  ticketPrice?: string
  createTime?: string
  updateTime?: string
}

export type ScenicAreaRecommendVO = ScenicArea & {
  score?: number
  reason?: string
}

export type Facility = {
  id: number
  name: string
  type?: string
  description?: string
  location?: string
  longitude?: number
  latitude?: number
  areaId?: number
}

export type FacilityNearbyVO = Facility & { distance?: number }

export type Food = {
  id: number
  name: string
  cuisine?: string
  description?: string
  price?: number
  rating?: number
  heat?: number
  restaurantId?: number
  areaId?: number
}

export type FoodRecommendVO = Food & { distance?: number; score?: number }

export type Diary = {
  id: number
  userId: number
  title: string
  content: string
  images?: string
  videos?: string
  heat?: number
  rating?: number
  createTime?: string
  updateTime?: string
}

export type DiaryDetailVO = Diary & {
  destinations?: number[]
  comments?: any[]
}

export type RoutePlanVO = { path: number[]; distance: number; time: number }

export async function apiRegister(payload: { username: string; password: string }) {
  const res = (await http.post('/api/auth/register', payload)) as ApiResponse<{
    user_id: number
    username: string
  }>
  return res.data
}

export async function apiLogin(payload: { username: string; password: string }) {
  const res = (await http.post('/api/auth/login', payload)) as ApiResponse<{
    token: string
    user: UserVO
  }>
  return res.data
}

export async function apiRefresh(token: string) {
  const res = (await http.post(
    '/api/auth/refresh',
    {},
    { headers: { Authorization: `Bearer ${token}` } },
  )) as ApiResponse<{ token: string }>
  return res.data
}

export async function apiUpdateInterest(payload: { interests: { type: string; value: string }[] }) {
  const res = (await http.put('/api/auth/interest', payload)) as ApiResponse<void>
  return res.data
}

export async function apiRecommendationList(params: {
  page?: number
  size?: number
  sortBy?: string
  type?: string
}) {
  const res = (await http.get('/api/recommendation', { params })) as ApiResponse<PageData<ScenicArea>>
  return res.data
}

export async function apiRecommendationPersonalized(params: { page?: number; size?: number; type?: string }) {
  const res = (await http.get('/api/recommendation/personalized', { params })) as ApiResponse<
    PageData<ScenicAreaRecommendVO>
  >
  return res.data
}

export async function apiRecommendationHot(params: { page?: number; size?: number; type?: string }) {
  const res = (await http.get('/api/recommendation/hot', { params })) as ApiResponse<PageData<ScenicArea>>
  return res.data
}

export async function apiScenicDetail(id: number) {
  const res = (await http.get(`/api/recommendation/detail/${id}`)) as ApiResponse<ScenicArea>
  return res.data
}

export async function apiMapData(params: { areaId?: number }) {
  const res = (await http.get('/api/route/map-data', { params })) as ApiResponse<{
    nodes: number[]
    edges: {
      startId: number
      endId: number
      distance: number
      speed: number
      congestion: number
      vehicleType?: string
    }[]
  }>
  return res.data
}

export async function apiPlanRoute(payload: {
  areaId?: number
  startId: number
  endId: number
  strategy?: 'distance' | 'time'
  vehicle?: 'walk' | 'bike' | 'shuttle' | string
}) {
  const res = (await http.post('/api/route', payload)) as ApiResponse<RoutePlanVO>
  return res.data
}

export async function apiPlanRouteMulti(payload: {
  areaId?: number
  points: number[]
  strategy?: 'distance' | 'time'
  vehicle?: 'walk' | 'bike' | 'shuttle' | string
}) {
  const res = (await http.post('/api/route/multi-point', payload)) as ApiResponse<RoutePlanVO>
  return res.data
}

export async function apiFacilityNearby(params: {
  lat: number
  lng: number
  radius?: number
  type?: string
  areaId?: number
}) {
  const res = (await http.get('/api/facility/nearby', { params })) as ApiResponse<FacilityNearbyVO[]>
  return res.data
}

export async function apiFacilitySearch(params: { keyword?: string; type?: string; areaId?: number; limit?: number }) {
  const res = (await http.get('/api/facility/search', { params })) as ApiResponse<Facility[]>
  return res.data
}

export async function apiFacilityDetail(id: number) {
  const res = (await http.get(`/api/facility/detail/${id}`)) as ApiResponse<Facility>
  return res.data
}

export async function apiFoodRecommendation(params: {
  areaId: number
  lat?: number
  lng?: number
  radius?: number
  wHeat?: number
  wRating?: number
  wDistance?: number
  page?: number
  size?: number
}) {
  const res = (await http.get('/api/food/recommendation', { params })) as ApiResponse<FoodRecommendVO[]>
  return res.data
}

export async function apiFoodSearch(params: {
  keyword?: string
  cuisine?: string
  areaId?: number
  page?: number
  size?: number
}) {
  const res = (await http.get('/api/food/search', { params })) as ApiResponse<Food[]>
  return res.data
}

export async function apiFoodDetail(id: number) {
  const res = (await http.get(`/api/food/detail/${id}`)) as ApiResponse<Food>
  return res.data
}

export async function apiFoodRate(payload: { foodId: number; rating: number; comment?: string }) {
  const res = (await http.post('/api/food/rate', payload)) as ApiResponse<void>
  return res.data
}

export async function apiDiaryCreate(payload: {
  title: string
  content: string
  images?: string[]
  videos?: string[]
  destinations?: number[]
}) {
  const res = (await http.post('/api/diary', {
    ...payload,
    images: payload.images ? JSON.stringify(payload.images) : undefined,
    videos: payload.videos ? JSON.stringify(payload.videos) : undefined,
  })) as ApiResponse<{ diary_id: number }>
  return res.data
}

export async function apiDiaryList(params: { page?: number; size?: number; sortBy?: string }) {
  const res = (await http.get('/api/diary', { params })) as ApiResponse<Diary[]>
  return res.data
}

export async function apiDiaryDetail(id: number) {
  const res = (await http.get(`/api/diary/${id}`)) as ApiResponse<DiaryDetailVO>
  return res.data
}

export async function apiDiaryUpdate(id: number, payload: { title: string; content: string; images?: string[]; videos?: string[] }) {
  const res = (await http.put(`/api/diary/${id}`, {
    ...payload,
    images: payload.images ? JSON.stringify(payload.images) : undefined,
    videos: payload.videos ? JSON.stringify(payload.videos) : undefined,
  })) as ApiResponse<void>
  return res.data
}

export async function apiDiaryDelete(id: number) {
  const res = (await http.delete(`/api/diary/${id}`)) as ApiResponse<void>
  return res.data
}

export async function apiDiarySearch(params: { keyword?: string; destination?: number; page?: number; size?: number }) {
  const res = (await http.get('/api/diary/search', { params })) as ApiResponse<Diary[]>
  return res.data
}

export async function apiDiaryRate(payload: { diaryId: number; rating: number }) {
  const res = (await http.post('/api/diary/rate', payload)) as ApiResponse<void>
  return res.data
}

export async function apiAdminAddScenicArea(payload: Partial<ScenicArea>) {
  const res = (await http.post('/api/admin/scenic-area', payload)) as ApiResponse<ScenicArea>
  return res.data
}

export async function apiAdminListScenicAreas(params: { page?: number; size?: number; type?: string }) {
  const res = (await http.get('/api/admin/scenic-area', { params })) as ApiResponse<PageData<ScenicArea>>
  return res.data
}

export async function apiAdminAddBuilding(payload: any) {
  const res = (await http.post('/api/admin/building', payload)) as ApiResponse<any>
  return res.data
}

export async function apiAdminAddRoad(payload: any) {
  const res = (await http.post('/api/admin/road', payload)) as ApiResponse<any>
  return res.data
}

export async function apiAdminAddFood(payload: any) {
  const res = (await http.post('/api/admin/food', payload)) as ApiResponse<any>
  return res.data
}

