package cn.zxJava.controller;

import cn.zxJava.domain.TbItemCat;
import cn.zxJava.entity.Result;
import cn.zxJava.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findByParentId/{parentId}")
    public Result findByParentId(@PathVariable("parentId") Long parentId){
        try {
            List<TbItemCat> list = itemCatService.findByParentId(parentId);
            return new Result(true,"操作成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TbItemCat> list = itemCatService.findAll();
            return new Result(true,"操作成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbItemCat tbItemCat){
        try {
            itemCatService.add(tbItemCat);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbItemCat tbItemCat){
        try {
            itemCatService.update(tbItemCat);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
