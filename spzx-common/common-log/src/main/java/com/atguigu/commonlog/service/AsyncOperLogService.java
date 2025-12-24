package com.atguigu.commonlog.service;

import com.atguigu.spzx.model.entity.system.SysOperLog;
import org.springframework.stereotype.Service;

@Service
public interface AsyncOperLogService {
    void saveSysOperLog(SysOperLog sysOperLog);


}
