package cn.zxJava.service.impl;


import cn.zxJava.domain.TbUser;
import cn.zxJava.entity.Result;
import cn.zxJava.mapper.TbUserMapper;
import cn.zxJava.service.UserService;
import cn.zxJava.utils.HttpClient;
import cn.zxJava.utils.RandomCode;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbUserMapper tbUserMapper;

    /*
    * 发送短信
    * */
    @Override
    public String sendCode(String phone) throws Exception {
        //生成验证码
        String code = RandomCode.genCode();
        System.out.println("生成的验证码为："+code);
        //存入redis,5分钟后失效
        redisTemplate.boundValueOps(phone).set(code,5, TimeUnit.MINUTES);

        //发送短信
        HttpClient client = new HttpClient("http://localhost:8087/sms/send/"+phone+"/"+code);
        client.get();
        //返回结果
        return client.getContent();
    }

    @Override
    public Result regist(TbUser tbUser, String code) {
        String  redisCode = (String) redisTemplate.boundValueOps(tbUser.getPhone()).get();
        //如果redisCode为空说明验证码已过期
        if (redisCode == null || !redisCode.equals(code)){
            return new Result(false,"验证码有误");
        }

        //实现注册功能，数据入库，密码加密（MD5）
        String password = tbUser.getPassword();
        String md5Pwd = DigestUtils.md5Hex(password);
        tbUser.setPassword(md5Pwd);
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        tbUserMapper.insert(tbUser);
        return new Result(true,"注册成功");
    }
}
