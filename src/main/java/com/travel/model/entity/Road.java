package com.travel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 道路实体，对应 roads 表。
 */
@TableName("roads")
public class Road implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("start_id")
    private Long startId;

    @TableField("end_id")
    private Long endId;

    private Double distance;

    private Double speed;

    /**
     * 拥挤度（0-1），真实速度 = 拥挤度 * 理想速度。
     */
    private Double congestion;

    @TableField("vehicle_type")
    private String vehicleType;

    /**
     * 所属景区/校园 ID（管理端接口需要）。
     */
    @TableField("area_id")
    private Long areaId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getStartId()
    {
        return startId;
    }

    public void setStartId(Long startId)
    {
        this.startId = startId;
    }

    public Long getEndId()
    {
        return endId;
    }

    public void setEndId(Long endId)
    {
        this.endId = endId;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public Double getSpeed()
    {
        return speed;
    }

    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    public Double getCongestion()
    {
        return congestion;
    }

    public void setCongestion(Double congestion)
    {
        this.congestion = congestion;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public Long getAreaId()
    {
        return areaId;
    }

    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public LocalDateTime getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime)
    {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime)
    {
        this.updateTime = updateTime;
    }
}

