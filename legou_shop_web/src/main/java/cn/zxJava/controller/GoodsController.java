package cn.zxJava.controller;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.entity.Result;
import cn.zxJava.groupentity.Goods;
import cn.zxJava.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/findPage/{pageNum}/{pageCode}")
    public Result findPage(@PathVariable("pageNum") int pageNum,@PathVariable("pageCode") int pageCode){
        try {
            //String  sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            PageInfo<TbGoods> pageInfo = goodsService.findPage(pageNum,pageCode);
            return new Result(true, "增加成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    @RequestMapping("/updateGoods/{val}/{ids}")
    public Result updateGoods(@PathVariable("val") String val, @PathVariable("ids") Long[] ids){
        try {

            goodsService.updateGoods(val,ids);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

}
