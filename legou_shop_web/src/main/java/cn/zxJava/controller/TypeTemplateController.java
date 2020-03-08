package cn.zxJava.controller;

import cn.zxJava.domain.TbTypeTemplate;
import cn.zxJava.entity.Result;
import cn.zxJava.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable int pageNum, @PathVariable int pageSize){
        try {
            PageInfo<TbTypeTemplate> pageInfo = typeTemplateService.findPage(pageNum,pageSize);
            return new Result(true,"操作成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbTypeTemplate tbTypeTemplate){
        try {
           typeTemplateService.add(tbTypeTemplate);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbTypeTemplate tbTypeTemplate){
        try {
            typeTemplateService.update(tbTypeTemplate);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){
        try {
            TbTypeTemplate tbTypeTemplate = typeTemplateService.findOne(id);
            return new Result(true,"操作成功",tbTypeTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable Long[] ids){
        try {
            typeTemplateService.delete(ids);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findSpecList/{id}")
    public Result findSpecList(@PathVariable("id") Long id){
        try {
            //根据模板查询规格和规格项数据
            List<Map> list = typeTemplateService.findSpecList(id);
            return new Result(true,"操作成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
