package com.travel.service;

import com.travel.model.entity.Facility;
import com.travel.model.vo.facility.FacilityNearbyVO;

import java.util.List;

/**
 * 设施查询服务。
 */
public interface FacilityService
{

    /**
     * 附近设施查询。
     *
     * @param lat     纬度
     * @param lng     经度
     * @param radius  半径（米）
     * @param type    类型过滤（可选）
     * @param areaId  景区/校园 ID（可选）
     * @return 设施列表（按距离排序）
     */
    List<FacilityNearbyVO> nearby(double lat, double lng, int radius, String type, Long areaId);

    /**
     * 关键字搜索设施（名称/类型）。
     */
    List<Facility> search(String keyword, String type, Long areaId, int limit);

    /**
     * 获取设施详情。
     */
    Facility detail(Long id);
}

