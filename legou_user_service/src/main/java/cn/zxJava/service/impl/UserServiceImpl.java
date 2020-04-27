package cn.zxJava.service.impl;


import cn.zxJava.domain.TbUser;
import cn.zxJava.entity.Result;
import cn.zxJava.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Override
    public String sendCode(String phone) throws Exception {
        return null;
    }

    @Override
    public Result regist(TbUser tbUser, String code) {
        return null;
    }
}
