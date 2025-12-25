package com.atguigu.spzx.manager.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

//    /**
//     * 只配置 MyBatis-Plus 拦截器
//     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 只配置分页插件
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }
//
//    /**
//     * 防止 PageHelper 污染（如果没删除 PageHelper 依赖）
//     */
//    @Bean
//    public GlobalConfig globalConfig() {
//        GlobalConfig globalConfig = new GlobalConfig();
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
//
//        // 设置逻辑删除
//        dbConfig.setLogicDeleteField("is_deleted");
//        dbConfig.setLogicDeleteValue("1");
//        dbConfig.setLogicNotDeleteValue("0");
//
//        // 设置主键策略（根据你的实体类）
//        dbConfig.setIdType(IdType.ASSIGN_ID);
//
//        globalConfig.setDbConfig(dbConfig);
//        return globalConfig;
//    }

}
