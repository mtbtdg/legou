package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import cn.zxJava.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    //paramMap是查询条件 {"keyword":'',"brand":'',"category":'',"page":1}
    public Result search(@RequestBody Map paramMap){
        try {
            // 搜索数据
            Map resultMap = itemSearchService.search(paramMap);
            return new Result(true,"查询成功",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }


    }

}
