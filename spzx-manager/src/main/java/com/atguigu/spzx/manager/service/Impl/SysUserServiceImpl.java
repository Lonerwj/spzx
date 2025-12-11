package com.atguigu.spzx.manager.service.Impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.SelfException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.properties.MinioProperties;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MinioProperties minioProperties ;

    @Override
    public LoginVo login(LoginDto loginDto) {

        // 1.校验验证码
        String codeKey = redisTemplate.opsForValue().get("user:login:validateCode:" + loginDto.getCodeKey());

        // 2.验证码不存在或错误
        if (StrUtil.isEmpty(codeKey) || !codeKey.equals(loginDto.getCaptcha())) {
            throw new SelfException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // 3.删除验证码
        redisTemplate.delete("user:login:validateCode:" + loginDto.getCodeKey());


        //1 获取用户信息
        SysUser sysUser = sysUserMapper.findByUserName(loginDto.getUserName());

        //2 校验用户信息
        if (sysUser == null) {
            throw new SelfException(ResultCodeEnum.LOGIN_ERROR);
        }

        //3 校验密码
        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        if (!sysUser.getPassword().equals(password)) {
            throw new SelfException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4 生成token并放入Redis
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String userInfo = JSON.toJSONString(sysUser);
        redisTemplate.opsForValue().set("user:login" + token, userInfo, 7, TimeUnit.DAYS);

        //5 封装返回
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    @Override
    public ValidateCodeVo generateVerifyCode() {
        //1.生成图形验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 50, 4, 5);
        String codeValue = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();

        // 生成uuid作为图片验证码的key
        String codeKey = UUID.randomUUID().toString().replace("-", "");

        // 将验证码存储到Redis中
        redisTemplate.opsForValue().set("user:login:validateCode:" + codeKey , codeValue , 5 , TimeUnit.MINUTES);

        // 构建响应结果数据
        ValidateCodeVo validateCodeVo = new ValidateCodeVo() ;
        validateCodeVo.setCodeKey(codeKey);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);

        // 返回数据
        return validateCodeVo;
    }

    @Override
    public SysUser getUserInfo(String token) {

//        //1.从Redis中获取token
//        String userInfo = redisTemplate.opsForValue().get("user:login" + token);
//        if (StrUtil.isEmpty(userInfo)) {
//            throw new SelfException(ResultCodeEnum.LOGIN_AUTH);
//        }
//        //2.根据token获取用户信息
//        SysUser sysUser = JSON.parseObject(userInfo, SysUser.class);
        //3.返回用户信息

        return AuthContextUtil.get();
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login" + token) ;
    }

    @Override
    public IPage<SysUser> findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize) {
        IPage pageParam = new Page<>(pageNum, pageSize);
        IPage<SysUser> page = sysUserMapper.findByPage(pageParam, sysUserDto);

        return page;
    }

    @Override
    public Result saveSysUser(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", sysUser.getUsername());
        if (this.count(queryWrapper) > 0) {
            return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        // 密码加密
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
        this.save(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateSysUser(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", sysUser.getUsername());
        if (this.count(queryWrapper) > 1) {
            return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        // 密码加密
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
        this.updateById(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {  // 如果存在打印信息
                System.out.println("Bucket 'spzx-bucket' already exists.");
            }

            // 设置存储对象名称
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //20230801/443e1e772bef482c95be28704bec58a901.jpg
            String fileName = dateDir+"/"+uuid+file.getOriginalFilename();
            System.out.println(fileName);

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build();
            minioClient.putObject(putObjectArgs) ;

            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName ;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
