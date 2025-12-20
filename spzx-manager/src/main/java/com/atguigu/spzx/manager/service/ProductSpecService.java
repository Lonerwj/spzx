package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ProductSpecService extends IService<ProductSpec>
{
    IPage<ProductSpec> findByPage(Integer page, Integer limit);
}
