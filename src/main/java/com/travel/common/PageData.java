package com.travel.common;

import java.util.List;

/**
 * 分页数据结构封装。
 *
 * @param <T> 列表元素类型
 */
public class PageData<T>
{

    private List<T> list;

    private Long total;

    public PageData()
    {
    }

    public PageData(List<T> list, Long total)
    {
        this.list = list;
        this.total = total;
    }

    public List<T> getList()
    {
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    public Long getTotal()
    {
        return total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }
}

