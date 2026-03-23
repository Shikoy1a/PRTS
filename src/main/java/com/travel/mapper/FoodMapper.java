package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Food;
import org.apache.ibatis.annotations.Mapper;

/**
 * 美食表 Mapper。
 */
@Mapper
public interface FoodMapper extends BaseMapper<Food>
{
}

