package cn.zxJava.controller;

import cn.zxJava.domain.TbUser;
import cn.zxJava.entity.Result;
import cn.zxJava.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /*
    * 发送短信
    * */
    @RequestMapping("/sendCode/{phone}")
    public String sendCode(@PathVariable("phone") String phone){
        String result = null;
        try {
            result = userService.sendCode(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 完成注册
     * */
    @RequestMapping("/regist/{code}")
    public Result regist(@PathVariable("code") String code, @RequestBody TbUser tbUser){
        return userService.regist(tbUser,code);
    }

}
