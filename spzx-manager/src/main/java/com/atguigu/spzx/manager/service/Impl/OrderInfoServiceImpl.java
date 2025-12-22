package com.atguigu.spzx.manager.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.manager.mapper.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.OrderStatisticsMapper;
import com.atguigu.spzx.manager.service.OrderInfoService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.atguigu.spzx.model.vo.order.OrderStatisticsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        QueryWrapper<OrderStatistics> wrapper = new QueryWrapper<>();
        if (orderStatisticsDto.getCreateTimeBegin() != null){
            wrapper.ge("create_time", orderStatisticsDto.getCreateTimeBegin());
        }
        if (orderStatisticsDto.getCreateTimeEnd() != null){
            wrapper.le("create_time", orderStatisticsDto.getCreateTimeEnd());
        }
        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.selectList(wrapper);

        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(orderStatisticsList.stream().map(orderStatistics -> DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd")).collect(Collectors.toList()));
        orderStatisticsVo.setAmountList(orderStatisticsList.stream().map(OrderStatistics::getTotalAmount).collect(Collectors.toList()));

        return orderStatisticsVo;
    }
}
