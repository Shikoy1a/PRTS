package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日记表 Mapper。
 */
@Mapper
public interface DiaryMapper extends BaseMapper<Diary>
{
}

