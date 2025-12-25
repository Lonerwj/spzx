package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.product.mapper.ProductDetailsMapper;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.atguigu.spzx.product.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public List<ProductSku> findHotProductSkuList() {

        return productMapper.findHotProductSkuList();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        IPage<ProductSku> pageParam = new Page<>(page, limit);
        IPage<ProductSku> pages = productMapper.findByPage(pageParam,productSkuDto);
        return PageUtils.toPageInfo(pages);
    }

    @Override
    public ProductItemVo getItem(Long skuId) {
        ProductItemVo productItemVo = new ProductItemVo();

        //1.通过skuId查询商品sku信息
        ProductSku productSku = productSkuMapper.selectById(skuId);
        productItemVo.setProductSku(productSku);

        //2.通过productSku获取商品信息
        Product product = productMapper.selectById(productSku.getProductId());
        productItemVo.setProduct(product);
        productItemVo.setSpecValueList(JSON.parseArray(productSku.getSkuSpec()));

        //3.将product中的轮播图用list存储
        List<String> sliderUrlList = Arrays.asList(product.getSliderUrls().split(","));
        productItemVo.setSliderUrlList(sliderUrlList);

        //4.将productDetails中的详情图片用list存储
        String detailsImageUrls = productDetailsMapper.selectOne(new QueryChainWrapper<>(ProductDetails.class).eq("product_id", product.getId())).getImageUrls();
        List<String> detailsImageUrlList = null;
        if (detailsImageUrls != null){
            detailsImageUrlList = Arrays.asList(detailsImageUrls.split(","));
        }
        productItemVo.setDetailsImageUrlList(detailsImageUrlList);

        //5.获取该商品的所有sku信息并封装到map中，key为skuSpec，value为skuId
        List<ProductSku> productSkuList = productSkuMapper.selectList(new QueryWrapper<ProductSku>().eq("product_id", product.getId()));
        Map<String,Object> skuSpecValueMap = new HashMap<>();
        for (ProductSku productSku1 : productSkuList) {
            skuSpecValueMap.put(productSku1.getSkuSpec(),productSku1.getId());
        }
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);

        return productItemVo;
    }
}
