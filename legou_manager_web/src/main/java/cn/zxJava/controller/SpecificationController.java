package cn.zxJava.controller;

import cn.zxJava.domain.Brand;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable Long id){
        try {
            Specification specification = specificationService.findOne(id);
            return new Result(true,"操作成功",specification);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Specification specification){
        try {
            specificationService.update(specification);
            return new Result(true,"操作成功",specification);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable Long[] ids){
        try {
            specificationService.delete(ids);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findSpecificationList")
    public Result findSpecificationList(){

        try {
            List<TbSpecification> list = specificationService.findAll();
            //创建一个集合,存储map
            List<Map<String ,Object>> mapList = new ArrayList<>();
            //遍历添加数据
            for (TbSpecification tbSpecification : list) {
                Map<String ,Object> map = new HashMap<>();
                map.put("id",tbSpecification.getId());
                map.put("text",tbSpecification.getSpecName());
                mapList.add(map);
            }
            return new Result(true,"操作成功",mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }
}
