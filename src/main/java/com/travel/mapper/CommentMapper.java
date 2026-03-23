package com.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论表 Mapper。
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment>
{
}

