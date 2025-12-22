package com.atguigu.spzx.manager.service.Impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.manager.service.ProductSkuService;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public IPage<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        Page<Product> pageParam = new Page<>(page, limit);
        return productMapper.findByPage(pageParam, productDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Product product) {

        //添加商品
        productMapper.insert(product);

        //添加商品sku
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuService.saveBatch(productSkuList);

        //添加商品详情
        ProductDetails productDetails = new ProductDetails(product.getId(), product.getDetailsImageUrls());
        productDetailsMapper.insert(productDetails);

        return true;
    }

    @Override
    public Product getById(Long id) {
        //通过Id查询product
        Product product = productMapper.selectById(id);

        //通过Id查询sku
        List<ProductSku> productSkuList = productSkuService.list(new QueryWrapper<ProductSku>().eq("product_id", id));
        product.setProductSkuList(productSkuList);

        //通过Id查询商品详情
        ProductDetails productDetails = productDetailsMapper.selectOne(new QueryWrapper<ProductDetails>().eq("product_id", id));
        product.setDetailsImageUrls(productDetails.getImageUrls());

        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Product product) {
        //修改product数据
        productMapper.updateById(product);

        //修改productSku数据
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuService.updateBatchById(productSkuList);

        //修改productDetails数据
        ProductDetails productDetails = productDetailsMapper.selectOne(new QueryWrapper<ProductDetails>().eq("product_id", product.getId()));
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.updateById(productDetails);

        return true;
    }

    @Override
    public void deleteById(Long id) {

        //删除product
        this.removeById(id);

        //删除productSku
        productSkuService.remove(new QueryWrapper<ProductSku>().eq("product_id", id));

        //删除productDetails
        productDetailsMapper.delete(new QueryWrapper<ProductDetails>().eq("product_id", id));
    }

    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = productMapper.selectById(id);

        if (auditStatus == 1) {
            product.setStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setStatus(-1);
            productMapper.updateById(product);
            product.setAuditMessage("审批未通过");
        }
        productMapper.updateById(product);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);

        if (status == 1){
            product.setStatus(1);
            product.setAuditMessage("上架成功");
        }else {
            product.setStatus(-1);
            product.setAuditMessage("下架成功");
        }
        productMapper.updateById(product);
    }
}
