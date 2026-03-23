package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Restaurant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 饭店表 Mapper。
 */
@Mapper
public interface RestaurantMapper extends BaseMapper<Restaurant>
{
}

