package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends IService<Category> {

    List<Category> findOneGradeCategory();

    List<Category> findCategoryTree();
}
