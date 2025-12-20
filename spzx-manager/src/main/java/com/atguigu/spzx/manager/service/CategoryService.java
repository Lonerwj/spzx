package com.atguigu.spzx.manager.service;


import cn.hutool.http.server.HttpServerResponse;
import com.atguigu.spzx.model.entity.product.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CategoryService extends IService<Category> {
    List<Category> findCategoryList(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file) throws IOException;
}
