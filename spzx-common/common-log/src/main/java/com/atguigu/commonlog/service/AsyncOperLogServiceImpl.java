package com.atguigu.commonlog.service;

import com.atguigu.commonlog.mapper.AsyncOperLogMapper;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService{

    @Autowired
    private AsyncOperLogMapper asyncOperLogMapper;

    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        asyncOperLogMapper.insert(sysOperLog);
    }
}
