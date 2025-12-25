package com.atguigu.spzx.product.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;

public class PageUtils {

    /**
     * 基础转换：IPage -> PageInfo（不转换数据类型）
     * @param iPage MyBatis-Plus 分页结果
     * @param navigatePages 导航页码数（默认为8）
     * @return PageInfo
     */
    public static <T> PageInfo<T> toPageInfo(IPage<T> iPage, int navigatePages) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setList(iPage.getRecords());
        pageInfo.setPageNum((int) iPage.getCurrent());
        pageInfo.setPageSize((int) iPage.getSize());
        pageInfo.setTotal(iPage.getTotal());
        pageInfo.setPages((int) iPage.getPages());
        pageInfo.calcByNavigatePages(navigatePages);
        return pageInfo;
    }

    // 简化版，使用默认导航页数
    public static <T> PageInfo<T> toPageInfo(IPage<T> iPage) {
        return toPageInfo(iPage, 8);
    }
}