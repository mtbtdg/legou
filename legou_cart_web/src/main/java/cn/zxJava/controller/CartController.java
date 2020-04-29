package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import cn.zxJava.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;



}
