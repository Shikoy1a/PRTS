package com.travel.algorithm.graph;

/**
 * 边权重计算函数。
 */
@FunctionalInterface
public interface EdgeWeightFunc
{
    double weight(Edge edge);
}

