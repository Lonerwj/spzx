package com.atguigu.spzx.manager.service.Impl;

import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public IPage<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        Page<Product> pageParam = new Page<>(page, limit);
        return productMapper.findByPage(pageParam, productDto);
    }
}
