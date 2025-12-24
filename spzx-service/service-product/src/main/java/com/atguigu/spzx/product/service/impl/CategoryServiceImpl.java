package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<Category> findOneGradeCategory() {

        //1 查询Redis中是否存在一级分类
        String categoryOneListJson = redisTemplate.opsForValue().get("category:one");

        //2 判断一级分类是否为空
        if (StringUtils.hasText(categoryOneListJson)) {

            return JSON.parseArray(categoryOneListJson, Category.class);
        }

        //3 查询数据库中的一级分类
        List<Category> categoryList = this.list(new QueryWrapper<Category>().eq("parent_id",0L));

        //4 缓存一级分类数据到Redis中
        redisTemplate.opsForValue().set("category:one", JSON.toJSONString(categoryList), 24, TimeUnit.HOURS);
        return categoryList;
    }

    @Cacheable(value = "category:tree", key = "'all'")
    @Override
    public List<Category> findCategoryTree() {

        //获取所有分类
        List<Category> categoryList = this.list();

        // 获取所有1级分类
        List<Category> categoryOneList =
                categoryList.stream().filter(category -> category.getParentId() == 0L).collect(Collectors.toList());

        // 根据1级分类，查询2级分类
        categoryOneList.forEach(category -> {
            List<Category> categoryTwoList =
                    categoryList.stream().filter(item -> item.getParentId() == category.getId()).collect(Collectors.toList());
            if (!categoryTwoList.isEmpty()){
                category.setChildren(categoryTwoList);
            }

            categoryTwoList.forEach(categoryTwo -> {
                List<Category> categoryThreeList =
                        categoryList.stream().filter(item -> item.getParentId() == categoryTwo.getId()).collect(Collectors.toList());
                if (!categoryThreeList.isEmpty()) {
                    categoryTwo.setChildren(categoryThreeList);
                }
            });
        });

        return categoryOneList;
    }

}
