package com.travel.model.vo.facility;

import com.travel.model.entity.Facility;

/**
 * 附近设施返回对象（包含距离信息）。
 */
public class FacilityNearbyVO
{

    private Facility facility;

    /**
     * 直线距离（米）。
     */
    private Double geoDistance;

    /**
     * 可达路径距离（米），如果未计算则为 null。
     */
    private Double pathDistance;

    public Facility getFacility()
    {
        return facility;
    }

    public void setFacility(Facility facility)
    {
        this.facility = facility;
    }

    public Double getGeoDistance()
    {
        return geoDistance;
    }

    public void setGeoDistance(Double geoDistance)
    {
        this.geoDistance = geoDistance;
    }

    public Double getPathDistance()
    {
        return pathDistance;
    }

    public void setPathDistance(Double pathDistance)
    {
        this.pathDistance = pathDistance;
    }
}

