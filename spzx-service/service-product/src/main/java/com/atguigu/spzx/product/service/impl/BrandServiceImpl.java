package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.product.mapper.BrandMapper;
import com.atguigu.spzx.product.service.BrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
}
