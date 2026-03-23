package com.travel.model.dto.facility;

import jakarta.validation.constraints.NotNull;

/**
 * 附近设施查询参数。
 */
public class FacilityNearbyQuery
{

    @NotNull(message = "lat 不能为空")
    private Double lat;

    @NotNull(message = "lng 不能为空")
    private Double lng;

    /**
     * 搜索半径（米）。
     */
    private Integer radius;

    /**
     * 设施类型过滤（如 洗手间/餐厅）。
     */
    private String type;

    /**
     * 所属景区/校园 ID（可选）。
     */
    private Long areaId;

    public Double getLat()
    {
        return lat;
    }

    public void setLat(Double lat)
    {
        this.lat = lat;
    }

    public Double getLng()
    {
        return lng;
    }

    public void setLng(Double lng)
    {
        this.lng = lng;
    }

    public Integer getRadius()
    {
        return radius;
    }

    public void setRadius(Integer radius)
    {
        this.radius = radius;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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

