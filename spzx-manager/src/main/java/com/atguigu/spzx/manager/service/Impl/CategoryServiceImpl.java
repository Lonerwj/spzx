package com.atguigu.spzx.manager.service.Impl;

import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.manager.listener.ExcelListener;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.excel.CategoryExcelVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> findCategoryList(Long id) {

        //查出所有菜单
        List<Category> categoryList = categoryMapper.selectList(new QueryWrapper<Category>().eq("parent_id", id).orderByAsc("id"));
        //看到菜单下面是否存在子菜单
        if (!categoryList.isEmpty()){
            categoryList.forEach(category -> {
                //找出下层分类
                List<Category> categoryList1 = categoryMapper.selectList(new QueryWrapper<Category>().eq("parent_id", category.getId()).orderByAsc("id"));
                category.setChildren(categoryList1);
                if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }
            });
        }

        return categoryList;
    }

    @Override
    public void exportData(HttpServletResponse response) {


        // 设置响应结果类型
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        // URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("分类数据", StandardCharsets.UTF_8);

        //设置响应头信息  Content-disposition:以下载方式打开
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


        //调用mapper方法查询所有分类，返回list集合
        List<Category> categoryList = categoryMapper.selectList(null);

        //构建excel数据
        List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
        categoryList.forEach(category -> {
            CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
            BeanUtils.copyProperties(category, categoryExcelVo);
            categoryExcelVoList.add(categoryExcelVo);
        });

        //调用EasyExcel的write方法完成写操作
        try {
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据")
                    .doWrite(categoryExcelVoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(MultipartFile file) throws IOException{

        //监听器
        ExcelListener listener = new ExcelListener(categoryMapper);

        EasyExcel.read(file.getInputStream(), CategoryExcelVo.class,listener)
                .sheet()
                .doRead();
    }
}
