package com.travel.algorithm.graph;

/**
 * 边过滤器（用于交通工具、道路权限等约束）。
 */
@FunctionalInterface
public interface EdgeFilter
{
    boolean allow(Edge edge);
}

