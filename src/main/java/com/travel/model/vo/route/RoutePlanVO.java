package com.travel.model.vo.route;

import java.util.List;

/**
 * 路线规划返回结果。
 */
public class RoutePlanVO
{

    private List<Long> path;

    /**
     * 总距离（米）。
     */
    private Double distance;

    /**
     * 预计时间（秒）。
     */
    private Double time;

    public List<Long> getPath()
    {
        return path;
    }

    public void setPath(List<Long> path)
    {
        this.path = path;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public Double getTime()
    {
        return time;
    }

    public void setTime(Double time)
    {
        this.time = time;
    }
}

