package com.travel.model.dto.route;

import jakarta.validation.constraints.NotNull;

/**
 * 两点路径规划请求。
 */
public class RoutePlanRequest
{

    @NotNull(message = "startId 不能为空")
    private Long startId;

    @NotNull(message = "endId 不能为空")
    private Long endId;

    /**
     * 策略：distance / time。
     */
    private String strategy;

    /**
     * 交通工具：walk / bike / shuttle。
     */
    private String vehicle;

    /**
     * 所属景区/校园 ID（用于限定道路范围，可选）。
     */
    private Long areaId;

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

    public String getStrategy()
    {
        return strategy;
    }

    public void setStrategy(String strategy)
    {
        this.strategy = strategy;
    }

    public String getVehicle()
    {
        return vehicle;
    }

    public void setVehicle(String vehicle)
    {
        this.vehicle = vehicle;
    }

    public Long getAreaId()
    {
        return areaId;
    }

    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }
}

