package cn.zxJava.service;

import cn.zxJava.domain.TbUser;
import cn.zxJava.entity.Result;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
public interface UserService {

    String sendCode(String phone) throws Exception;

    Result regist(TbUser tbUser, String code);
}
