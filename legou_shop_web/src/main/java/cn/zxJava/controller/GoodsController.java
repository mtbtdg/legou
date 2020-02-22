package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import cn.zxJava.groupentity.Goods;
import cn.zxJava.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @RequestMapping("/save")
    public Result save(@RequestBody Goods goods){
        try {
            String  sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getTbGoods().setSellerId(sellerId);
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

}
