package cn.zxJava.controller;

import cn.zxJava.domain.TbAddress;
import cn.zxJava.entity.Result;
import cn.zxJava.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    /**
     * 查询登录用户的收获地址
     **/
    @RequestMapping("/findAddressList")
    public Result findAddressList(){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<TbAddress> addressList = addressService.findAddressList(username);
            return new Result(true,"操作成功",addressList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

}
