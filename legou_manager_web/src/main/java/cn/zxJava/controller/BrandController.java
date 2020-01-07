package cn.zxJava.controller;

import cn.zxJava.domain.Brand;
import cn.zxJava.entity.Result;
import cn.zxJava.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // = @Controller + @ResponseBody
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable int pageNum, @PathVariable int pageSize){
        try {
            PageInfo<Brand> pageInfo = brandService.findPage(pageNum,pageSize);
            //返回成功的结果
            return new Result(true,"查询成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            //打印错误信息到控制台
            e.printStackTrace();
            //返回错误结果
            return new Result(false,"查询失败");
        }
    }

    @RequestMapping("/save")
    public Result save(@RequestBody Brand brand){
        try {
            brandService.save(brand);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){
        try {
            Brand brand = brandService.findOne(id);
            return new Result(true,"操作成功",brand);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long [] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
