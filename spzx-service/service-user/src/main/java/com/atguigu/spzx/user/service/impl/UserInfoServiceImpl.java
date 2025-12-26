package com.atguigu.spzx.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.common.exception.SelfException;
import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.mapper.UserInfoMapper;
import com.atguigu.spzx.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void register(UserRegisterDto userRegisterDto) {

        //校验验证码
        String code = redisTemplate.opsForValue().get(userRegisterDto.getUsername());

        if (code == null) {
            throw new SelfException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        if (!code.equals(userRegisterDto.getCode())) {
            throw new SelfException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //保存用户对象
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterDto.getUsername());
        userInfo.setPassword(DigestUtils.md5DigestAsHex(userRegisterDto.getPassword().getBytes()));
        userInfo.setNickName(userRegisterDto.getNickName());
        userInfo.setPhone(userRegisterDto.getUsername());
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("https://cn.bing.com/images/search?q=%e5%9b%be%e7%89%87&id=23D4844C3742FB976B138C2B7D51BE88FA1BAA89&FORM=IQFRBA");
        save(userInfo);

    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        //校验用户名
        UserInfo userInfo = getOne(new QueryWrapper<UserInfo>().eq("username", userLoginDto.getUsername()));
        if (userInfo == null) {
            throw new SelfException(ResultCodeEnum.LOGIN_ERROR);
        }
        //校验密码
        String password = DigestUtils.md5DigestAsHex(userLoginDto.getPassword().getBytes());
        if (!userInfo.getPassword().equals(password)) {
            throw new SelfException(ResultCodeEnum.LOGIN_ERROR);
        }
        //查看用户是否禁用
        if (userInfo.getStatus() == 0) {
            throw new SelfException(ResultCodeEnum.ACCOUNT_STOP);
        }

        //生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:spzx:" + token, JSON.toJSONString(userInfo), 30, TimeUnit.DAYS);

        //删除验证码
        redisTemplate.delete(userLoginDto.getUsername());

        return token;
    }

    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        String userInfoJSON = redisTemplate.opsForValue().get("user:spzx:" + token);
        if(StringUtils.isEmpty(userInfoJSON)) {
            throw new SelfException(ResultCodeEnum.LOGIN_AUTH) ;
        }
        UserInfo userInfo = JSON.parseObject(userInfoJSON , UserInfo.class) ;
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo ;
    }
}
