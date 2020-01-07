package cn.zxJava.controller;

import cn.zxJava.domain.TbSpecification;
import cn.zxJava.entity.Result;
import cn.zxJava.groupentity.Specification;
import cn.zxJava.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable int pageNum, @PathVariable int pageSize){
        try {
            PageInfo<TbSpecification> pageInfo = specificationService.findPage(pageNum,pageSize);
            return new Result(true,"操作成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/save")
    public Result save(@RequestBody Specification specification){
        try {
            specificationService.save(specification);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
