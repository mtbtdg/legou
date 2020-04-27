package cn.zxJava.controller;

import cn.zxJava.domain.TbUser;
import cn.zxJava.entity.Result;
import cn.zxJava.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
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

    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("/sendCode/{phone}")
    public String sendCode(@PathVariable("phone") String phone){
        try {
            // 发送验证码
            String result = userService.sendCode(phone);
            // 获取结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 注册
     */
    @RequestMapping("/regist/{code}")
    public Result regist(@PathVariable("code") String code,@RequestBody TbUser tbUser){
        try {
            // 进行用户注册
            Result result = userService.regist(tbUser, code);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }

}
