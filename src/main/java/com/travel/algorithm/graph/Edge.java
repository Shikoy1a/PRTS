package com.travel.algorithm.graph;

/**
 * 图边结构（邻接表边）。
 */
public class Edge
{

    private final long targetId;

    private final double distance;

    private final double speed;

    private final double congestion;

    private final String vehicleType;

    public Edge(long targetId, double distance, double speed, double congestion, String vehicleType)
    {
        this.targetId = targetId;
        this.distance = distance;
        this.speed = speed;
        this.congestion = congestion;
        this.vehicleType = vehicleType;
    }

    public long getTargetId()
    {
        return targetId;
    }

    public double getDistance()
    {
        return distance;
    }

    public double getSpeed()
    {
        return speed;
    }

    public double getCongestion()
    {
        return congestion;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }
}

