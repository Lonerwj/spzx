package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService extends IService<UserInfo> {
    void register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
