package com.travel.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Top-K 选择器。
 *
 * <p>
 * 用于在不进行完全排序的情况下，从候选集中选出分数最高的前 K 个元素。
 * </p>
 */
public class TopKSelector<T>
{

    /**
     * 选出 Top-K。
     *
     * @param items      候选列表
     * @param k          K 值
     * @param comparator 比较器（分数越大越靠前）
     * @return Top-K（按 comparator 从高到低排序后的结果）
     */
    public List<T> selectTopK(List<T> items, int k, Comparator<T> comparator)
    {
        if (items == null || items.isEmpty() || k <= 0)
        {
            return List.of();
        }
        int kk = Math.min(k, items.size());

        // 小顶堆：堆顶是当前 TopK 中“最差”的那个
        PriorityQueue<T> pq = new PriorityQueue<>(kk, comparator);
        for (T item : items)
        {
            if (pq.size() < kk)
            {
                pq.offer(item);
            }
            else
            {
                // comparator: bigger is better. if item better than worst (peek), replace.
                if (comparator.compare(item, pq.peek()) > 0)
                {
                    pq.poll();
                    pq.offer(item);
                }
            }
        }

        List<T> result = new ArrayList<>(pq.size());
        while (!pq.isEmpty())
        {
            result.add(pq.poll());
        }

        // pq 弹出是从“最差”到“最好”，这里反转得到从“最好”到“最差”
        result.sort(comparator.reversed());
        return result;
    }
}

