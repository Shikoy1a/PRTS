package com.travel.service;

import com.travel.common.PageData;
import com.travel.model.entity.Building;
import com.travel.model.entity.Food;
import com.travel.model.entity.Road;
import com.travel.model.entity.ScenicArea;

/**
 * 管理端数据管理服务。
 */
public interface AdminService
{

    ScenicArea addScenicArea(ScenicArea scenicArea);

    PageData<ScenicArea> listScenicAreas(Integer page, Integer size, String type);

    Building addBuilding(Building building);

    Road addRoad(Road road);

    Food addFood(Food food);
}

