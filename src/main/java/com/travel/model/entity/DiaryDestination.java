package com.travel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日记-目的地关联实体，对应 diary_destinations 表。
 */
@TableName("diary_destinations")
public class DiaryDestination implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("diary_id")
    private Long diaryId;

    @TableField("destination_id")
    private Long destinationId;

    @TableField("create_time")
    private LocalDateTime createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getDiaryId()
    {
        return diaryId;
    }

    public void setDiaryId(Long diaryId)
    {
        this.diaryId = diaryId;
    }

    public Long getDestinationId()
    {
        return destinationId;
    }

    public void setDestinationId(Long destinationId)
    {
        this.destinationId = destinationId;
    }

    public LocalDateTime getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime)
    {
        this.createTime = createTime;
    }
}

