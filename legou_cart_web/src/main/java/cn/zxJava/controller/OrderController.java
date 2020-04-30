package cn.zxJava.controller;


import cn.zxJava.domain.TbOrder;
import cn.zxJava.entity.Result;
import cn.zxJava.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 订单模块
* */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/saveOrder")
    //传过来的tbOrder只获取所有order的公用数据部分（比如userId，address等），每个order的实际数据通过redis查询cart获取
    public Result saveOrder(@RequestBody TbOrder tbOrder) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            tbOrder.setUserId(username);
            orderService.saveOrder(tbOrder);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }

    }

}
