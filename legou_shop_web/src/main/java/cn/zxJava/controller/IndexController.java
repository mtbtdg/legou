package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/findLoginname")
    public Result findLoginname(){
        try {
            //从springsecurity框架中获取用户名
            //上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            //获取用户名
            String loginName = context.getAuthentication().getName();

            return new Result(true,"操作成功",loginName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
